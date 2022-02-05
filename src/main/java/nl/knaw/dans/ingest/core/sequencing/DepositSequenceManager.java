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
package nl.knaw.dans.ingest.core.sequencing;

import nl.knaw.dans.ingest.core.Deposit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;

/**
 * Manages the process of ingesting deposits in the correct order by ensuring that deposits that target the same dataset are not concurrently scheduled on different threads. If an unfinished deposit
 * for the same dataset is still present, the next deposit for that dataset will be queued on the same thread, ensuring that it cannot overtake the already processing deposit.
 */
public class DepositSequenceManager {
    private static final Logger log = LoggerFactory.getLogger(DepositSequenceManager.class);
    private final LinkedHashMap<String, DepositSequencer> editors = new LinkedHashMap<>();
    private final ExecutorService executorService;

    public DepositSequenceManager(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public synchronized void scheduleDeposit(Deposit deposit) {
        log.trace("Enqueuing deposit");
        DepositSequencer editor = editors.get(deposit.getDoi());
        if (editor == null) {
            log.debug("Creating NEW editor for DOI {}", deposit.getDoi());
            editor = new DepositSequencer(this, deposit);
            editors.put(deposit.getDoi(), editor);
            executorService.execute(editor);
        }
        else {
            log.debug("Using EXISTING editor for DOI {}", deposit.getDoi());
            editor.enqueue(deposit);
        }
    }

    synchronized void removeEditor(DepositSequencer editor) {
        log.trace("Removing editor for DOI {}", editor.getTargetDoi());
        editors.remove(editor.getTargetDoi());
    }

}