/*
 * Copyright (C) 2022 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.ingest.core;

import nl.knaw.dans.ingest.core.legacy.DepositImportTaskWrapper;
import nl.knaw.dans.ingest.core.legacy.DepositIngestTaskFactoryWrapper;
import nl.knaw.dans.ingest.core.sequencing.TargettedTaskSequenceManager;
import nl.knaw.dans.ingest.core.service.TaskEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ImportInbox {
    private static final Logger log = LoggerFactory.getLogger(ImportInbox.class);
    private final Path inboxDir;
    private final DepositIngestTaskFactoryWrapper taskFactory;
    private final TargettedTaskSequenceManager targettedTaskSequenceManager;
    private final TaskEventService taskEventService;
    private final ExecutorService enqueingExecutor = Executors.newSingleThreadExecutor();

    public ImportInbox(Path inboxDir, DepositIngestTaskFactoryWrapper taskFactory,
        TargettedTaskSequenceManager targettedTaskSequenceManager, TaskEventService taskEventService) {
        this.inboxDir = inboxDir;
        this.taskFactory = taskFactory;
        this.targettedTaskSequenceManager = targettedTaskSequenceManager;
        this.taskEventService = taskEventService;
    }

    public void importBatch(Path batch, boolean continuePrevious) {
        Path batchDir = inboxDir.resolve(batch);
        validateBatchDir(batchDir);
        taskFactory.setOutbox(initOutbox(batch, continuePrevious));
        // Enqueuing can take a while; in order to return to the caller as soon as possible we dispatch this to dedicated thread
        enqueingExecutor.execute(() -> {
            try {
                Files.list(batchDir)
                    .map(d -> taskFactory.createIngestTask(d, taskEventService))
                    .sorted()
                    .collect(Collectors.toList())
                    .forEach(this::enqueue);
            }
            catch (IOException e) {
                // TODO: mark batch as "failed to start"
                e.printStackTrace();
            }
        });
    }

    private void enqueue(DepositImportTaskWrapper w) {
        log.trace("Enqueuing {}", w);
        try {
            targettedTaskSequenceManager.scheduleTask(w);
            taskEventService.writeEvent(w.getDepositId(), TaskEvent.EventType.ENQUEUE, TaskEvent.Result.OK, null);
        }
        catch (Exception e) {
            log.error("Enqueing of {} failed", w, e);
            taskEventService.writeEvent(w.getDepositId(), TaskEvent.EventType.ENQUEUE, TaskEvent.Result.FAILED, e.getMessage());
        }
    }

    private void validateBatchDir(Path batchDir) {
        if (Files.isRegularFile(batchDir))
            throw new IllegalArgumentException("Batch directory is a regular file: " + batchDir);
        if (!Files.exists(batchDir))
            throw new IllegalArgumentException("Batch directory does not exist: " + batchDir);
    }

    private Path initOutbox(Path batch, boolean continuePrevious) {
        Path outbox = taskFactory.getOutbox().resolve(batch);
        try {
            Path processedDir = outbox.resolve("processed");
            Path failedDir = outbox.resolve("failed");
            Path rejectedDir = outbox.resolve("rejected");

            Files.createDirectories(outbox);
            Files.createDirectories(processedDir);
            Files.createDirectories(failedDir);
            Files.createDirectories(rejectedDir);

            if (!continuePrevious && (nonEmpty(processedDir) || nonEmpty(failedDir) || nonEmpty(rejectedDir))) {
                throw new IllegalArgumentException("outbox is not empty; start with empty outbox at " + outbox + ", or use the 'continue' option");
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException("cannot initialize outbox for batch at " + outbox, e);
        }
        return outbox;
    }

    private boolean nonEmpty(Path p) throws IOException {
        return Files.list(p).findAny().isPresent();
    }

    // TODO: implement getStatus (base on list of tasks? on database?)
}
