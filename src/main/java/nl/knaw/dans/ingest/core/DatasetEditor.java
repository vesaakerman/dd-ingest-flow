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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DatasetEditor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(DatasetEditor.class);
    private final Queue<Deposit> depositQueue2 = new ConcurrentLinkedDeque<>();
    private final DatasetEditorMap datasetEditorMap;
    private final String targetDoi;

    public DatasetEditor(DatasetEditorMap datasetEditorMap, Deposit deposit) {
        this.datasetEditorMap = datasetEditorMap;
        this.targetDoi = deposit.getDoi();
        enqueue(deposit);
    }

    public synchronized void enqueue(Deposit deposit) {
        log.debug("Adding deposit {} to editor queue", deposit);
        if (deposit.getDoi().equals(targetDoi)) {
            depositQueue2.add(deposit);
        }
        else {
            throw new IllegalArgumentException("Deposit DOI " + deposit.getDoi() + " is different from target DOI " + targetDoi);
        }
    }

    public String getTargetDoi() {
        return targetDoi;
    }

    @Override
    public void run() {
        Deposit deposit = depositQueue2.poll();

        while (deposit != null) {
            log.debug("Processing deposit {}", deposit);
            try {
                Thread.sleep(deposit.getDelay());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            deposit = getNextDeposit();
        }
    }

    private Deposit getNextDeposit() {
        synchronized (datasetEditorMap) {
            Deposit deposit = depositQueue2.poll();
            if (deposit == null) {
                log.debug("No more deposits. Removing editor for DOI {}", targetDoi);
                datasetEditorMap.removeEditor(this);
            }
            return deposit;
        }
    }

}
