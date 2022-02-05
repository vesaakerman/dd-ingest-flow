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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;

public class DatasetEditorMap {
    private static final Logger log = LoggerFactory.getLogger(DatasetEditorMap.class);
    private final LinkedHashMap<String, DatasetEditor> editors = new LinkedHashMap<>();
    private final ExecutorService executorService;

    public DatasetEditorMap(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public synchronized void enqueueDeposit(Deposit deposit) {
        log.trace("Enqueuing deposit");
        DatasetEditor editor = editors.get(deposit.getDoi());
        if (editor == null) {
            log.debug("Creating NEW editor for DOI {}", deposit.getDoi());
            editor = new DatasetEditor(this, deposit);
            editors.put(deposit.getDoi(), editor);
            executorService.execute(editor);
        }
        else {
            log.debug("Using EXISTING editor for DOI {}", deposit.getDoi());
            editor.enqueue(deposit);
        }
    }

    public synchronized void removeEditor(DatasetEditor editor) {
        log.trace("Removing editor for DOI {}", editor.getTargetDoi());
        editors.remove(editor.getTargetDoi());
    }

}
