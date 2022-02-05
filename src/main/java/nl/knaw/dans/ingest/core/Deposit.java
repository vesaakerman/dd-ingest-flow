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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.IOException;
import java.nio.file.Path;

public class Deposit {
    private final int delay;
    private final String doi;
    private final String filename;
    private final PropertiesConfiguration configuration = new PropertiesConfiguration();

    public Deposit(Path path) throws IOException, ConfigurationException {
        configuration.load(path.toFile());
        filename = path.getFileName().toString();
        delay = configuration.getInteger("delay", 0);
        doi = configuration.getString("doi");
    }

    public String getDoi() {
        return doi;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public String toString() {
        return "Deposit{" +
            "filename=" + filename +
            "delay=" + delay +
            ", configuration=" + configuration +
            '}';
    }
}
