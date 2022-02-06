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

import nl.knaw.dans.ingest.core.legacy.DepositIngestTaskFactoryWrapper;
import nl.knaw.dans.ingest.core.sequencing.TargettedTaskSequenceManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

// TODO: use Managed to cleanly interrupt when stopping service?
public class ImportInbox extends AbstractInbox {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public ImportInbox(Path inboxDir, DepositIngestTaskFactoryWrapper taskFactory,
        TargettedTaskSequenceManager targettedTaskSequenceManager) {
        super(inboxDir, taskFactory, targettedTaskSequenceManager);
    }

    public void startBatch(Path batch) {
        Path batchDir = inboxDir.resolve(batch);
        validateBatchDir(batchDir);
        taskFactory.setOutbox(initOutbox(batch));
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Files.list(batchDir)
                        .map(taskFactory::createIngestTask)
                        .sorted()
                        .collect(Collectors.toList())
                        .forEach(targettedTaskSequenceManager::scheduleTask);
                }
                catch (IOException e) {
                    // TODO: mark batch as "failed to start"
                    e.printStackTrace();
                }
            }
        });
    }

    private void validateBatchDir(Path batchDir) {
        if (Files.isRegularFile(batchDir)) throw new IllegalArgumentException("Batch directory is a regular file: " + batchDir);
        if (!Files.exists(batchDir)) throw new IllegalArgumentException("Batch directory does not exist: " + batchDir);
    }

    private Path initOutbox(Path batch) {
        Path outbox = taskFactory.getOutbox().resolve(batch);
        try {
            Files.createDirectories(outbox);
            Files.createDirectories(outbox.resolve("processed"));
            Files.createDirectories(outbox.resolve("failed"));
            Files.createDirectories(outbox.resolve("rejected"));
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Cannot initialize outbox for batch at " + outbox, e);
        }
        return outbox;
    }


    // TODO: implement getStatus (base on list of tasks? on database?)
}
