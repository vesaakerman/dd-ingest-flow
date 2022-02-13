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
package nl.knaw.dans.ingest.core.service;

import nl.knaw.dans.ingest.core.legacy.DepositImportTaskWrapper;
import nl.knaw.dans.ingest.core.legacy.DepositIngestTaskFactoryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BatchImpl implements Batch {
    private static final Logger log = LoggerFactory.getLogger(BatchImpl.class);

    private final String name;
    private final Path dir;
    private final EventWriter eventWriter;
    private final DepositIngestTaskFactoryWrapper taskFactory;

    private boolean getTasksCalled = false;
    private boolean getTasksFailed = false;

    public BatchImpl(String name, Path dir, TaskEventService taskEventService, DepositIngestTaskFactoryWrapper taskFactory) {
        this.name = name;
        if (!dir.isAbsolute())
            throw new IllegalArgumentException("dir must be an absolute path");
        this.dir = dir;
        this.eventWriter = new EventWriter(taskEventService, name);
        this.taskFactory = taskFactory;
    }

    @Override
    public EventWriter getEventWriter() {
        return eventWriter;
    }

    @Override
    public List<DepositImportTaskWrapper> getTasks() {
        if (getTasksCalled)
            throw new IllegalStateException("getTasks should be called only once per batch");
        try {
            return Files.list(dir)
                .map(d -> taskFactory.createIngestTask(d, eventWriter))
                .sorted()
                .collect(Collectors.toList());
        }
        catch (IOException e) {
            log.error("Could not create tasks for batch {}", name, e);
            getTasksFailed = true;
            return Collections.emptyList();
        }
        finally {
            getTasksCalled = true;
        }
    }

    @Override
    public boolean isFailed() {
        return getTasksFailed;
    }

}
