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

import nl.knaw.dans.ingest.core.sequencing.DepositSequenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public abstract class AbstractInbox {
    private static final Logger log = LoggerFactory.getLogger(AbstractInbox.class);
    protected final Path inboxDir;
    protected final Path outBoxDir;
    protected final DepositSequenceManager depositSequenceManager;

    public AbstractInbox(Path inboxDir, Path outBoxDir, DepositSequenceManager depositSequenceManager) {
        this.inboxDir = inboxDir;
        this.outBoxDir = outBoxDir;
        this.depositSequenceManager = depositSequenceManager;
    }
}
