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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import nl.knaw.dans.ingest.core.config.DataverseConfigScala;
import nl.knaw.dans.ingest.core.config.HttpServiceConfig;
import nl.knaw.dans.ingest.core.config.IngestConfig;

public class DdIngestFlowConfiguration extends Configuration {

    private IngestConfig importConf;
    private IngestConfig autoIngest;
    private DataverseConfigScala dataverse;
    private HttpServiceConfig validateDansBag;
    private HttpServiceConfig managePrestaging;

    @JsonProperty("import")
    public IngestConfig getImportConf() {
        return importConf;
    }

    @JsonProperty("import")
    public void setImportConf(IngestConfig importConf) {
        this.importConf = importConf;
    }

    public IngestConfig getAutoIngest() {
        return autoIngest;
    }

    public void setAutoIngest(IngestConfig autoIngest) {
        this.autoIngest = autoIngest;
    }

    public DataverseConfigScala getDataverse() {
        return dataverse;
    }

    public void setDataverse(DataverseConfigScala dataverse) {
        this.dataverse = dataverse;
    }

    public HttpServiceConfig getValidateDansBag() {
        return validateDansBag;
    }

    public void setValidateDansBag(HttpServiceConfig validateDansBag) {
        this.validateDansBag = validateDansBag;
    }

    public HttpServiceConfig getManagePrestaging() {
        return managePrestaging;
    }

    public void setManagePrestaging(HttpServiceConfig managePrestaging) {
        this.managePrestaging = managePrestaging;
    }
}
