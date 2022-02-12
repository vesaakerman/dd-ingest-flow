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
package nl.knaw.dans.ingest.core.legacy;

import gov.loc.repository.bagit.domain.Bag;
import gov.loc.repository.bagit.domain.Metadata;
import nl.knaw.dans.easy.dd2d.DepositIngestTask;
import nl.knaw.dans.ingest.core.sequencing.TargettedTask;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

public class DepositImportTaskWrapper implements TargettedTask, Comparable<DepositImportTaskWrapper> {
    private final DepositIngestTask task;

    public DepositImportTaskWrapper(DepositIngestTask task) {
        this.task = task;
    }

    @Override
    public String getTarget() {
        return task.deposit().doi();
    }

    @Override
    public void run() {
        // Save start time to database
        task.run();
        // Save end time + result to database
    }

    @Override
    public int compareTo(DepositImportTaskWrapper o) {
        return getCreatedInstant(task).compareTo(getCreatedInstant(o.task));
    }

    private static Instant getCreatedInstant(DepositIngestTask t) {
        Bag bag = null;
        try {
            bag = t.deposit().tryBag().get();
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Unable to find bag; task = " + t, e);
        }
        Metadata metadata = bag.getMetadata();
        if (metadata == null) {
            throw new IllegalArgumentException("bag-info.txt not found in bag; task = " +  t);
        }
        List<String> createdValues = metadata.get("Created");
        if (createdValues == null) {
            throw new IllegalArgumentException("No Created value found in bag; task = " + t);
        }
        if (createdValues.size() != 1) {
            throw new IllegalArgumentException("There should be exactly one Created value; found " + createdValues.size() + "; task = " + t);
        }
        return OffsetDateTime.parse(createdValues.get(0)).toInstant();
    }

    @Override
    public String toString() {
        return "DepositImportTaskWrapper{" +
            "task=" + task +
            '}';
    }
}