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
package nl.knaw.dans.ingest.core.config;

import nl.knaw.dans.lib.util.ExecutorServiceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;

public class IngestConfig {
    @NotNull
    @Valid
    private Path inbox;

    @NotNull
    @Valid
    private Path outbox;

    @NotNull
    @Valid
    private Path zipWrappingTempDir;

    @NotNull
    @Valid
    private String fileExclusionPattern;

    @NotNull
    @Valid
    private String depositorRole;

    @Valid
    private boolean deduplicate;

    @NotNull
    @Valid
    private ExecutorServiceFactory taskQueue;

    public Path getInbox() {
        return inbox;
    }

    public void setInbox(Path inbox) {
        this.inbox = inbox;
    }

    public Path getOutbox() {
        return outbox;
    }

    public void setOutbox(Path outbox) {
        this.outbox = outbox;
    }

    public Path getZipWrappingTempDir() {
        return zipWrappingTempDir;
    }

    public void setZipWrappingTempDir(Path zipWrappingTempDir) {
        this.zipWrappingTempDir = zipWrappingTempDir;
    }

    public String getFileExclusionPattern() {
        return fileExclusionPattern;
    }

    public void setFileExclusionPattern(String fileExclusionPattern) {
        this.fileExclusionPattern = fileExclusionPattern;
    }

    public String getDepositorRole() {
        return depositorRole;
    }

    public void setDepositorRole(String depositorRole) {
        this.depositorRole = depositorRole;
    }

    public boolean isDeduplicate() {
        return deduplicate;
    }

    public void setDeduplicate(boolean deduplicate) {
        this.deduplicate = deduplicate;
    }

    public ExecutorServiceFactory getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(ExecutorServiceFactory taskQueue) {
        this.taskQueue = taskQueue;
    }
}
