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

package nl.knaw.dans.ingest;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.knaw.dans.ingest.core.ingestscheduling.DepositIngestManager;
import nl.knaw.dans.ingest.core.Inbox;

import java.util.concurrent.ExecutorService;

public class DdIngestFlowApplication extends Application<DdIngestFlowConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DdIngestFlowApplication().run(args);
    }

    @Override
    public String getName() {
        return "DD Ingest Flow";
    }

    @Override
    public void initialize(final Bootstrap<DdIngestFlowConfiguration> bootstrap) {
    }

    @Override
    public void run(final DdIngestFlowConfiguration configuration, final Environment environment) {
        final ExecutorService taskExecutor = configuration.getIngest().getTaskQueue().build(environment);
        final DepositIngestManager depositIngestManager = new DepositIngestManager(taskExecutor);
        final Inbox inbox = new Inbox(configuration.getIngest().getImportBaseDir(), depositIngestManager);
        environment.lifecycle().manage(inbox);
    }

}
