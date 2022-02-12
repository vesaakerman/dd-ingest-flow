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

import better.files.File;
import nl.knaw.dans.easy.dd2d.Deposit;
import nl.knaw.dans.easy.dd2d.DepositMigrationTask;
import nl.knaw.dans.easy.dd2d.ZipFileHandler;
import nl.knaw.dans.ingest.core.legacy.DepositImportTaskWrapper;
import org.junit.jupiter.api.Test;
import scala.Option;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositImportTaskWrapperTest {
    private static final Path testDepositsBasedir = Paths.get("src/test/resources/unordered-stub-deposits/");

    @Test
    public void depositsShouldBeOrderedByCreatedTimestamp() {
        List<DepositImportTaskWrapper> sorted = Stream.of(
            createTaskWrapper("deposit1_b"),
            createTaskWrapper("deposit1_a"),
            createTaskWrapper("deposit1_first")
        ).sorted().collect(Collectors.toList());

        assertEquals("10.5072/deposit1_first", sorted.get(0).getTarget());
        assertEquals("10.5072/deposit1_a", sorted.get(1).getTarget());
        assertEquals("10.5072/deposit1_b", sorted.get(2).getTarget());
    }

    private static DepositImportTaskWrapper createTaskWrapper(String depositName) {
        return new DepositImportTaskWrapper(new DepositMigrationTask(
            new Deposit(File.apply(testDepositsBasedir.resolve(depositName))),
            Option.empty(),
            new ZipFileHandler(File.apply(Paths.get("dummy"))),
            "dummy",
            false,
            null,
            Option.empty(),
            null,
            Option.empty(),
            0,
            0,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        ));
    }
}
