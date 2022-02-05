package nl.knaw.dans.ingest.core.legacy;

import nl.knaw.dans.easy.dd2d.DepositIngestTask;
import nl.knaw.dans.ingest.core.config.IngestConfig;

import java.nio.file.Path;

/**
 * Wraps the legacy Scala ingest task factory
 */
public class DepositIngestTaskFactoryWrapper {

    public DepositIngestTaskFactoryWrapper(IngestConfig ingestConfig) {
    }

    public DepositIngestTask createIngestTask(Path depositDir) {
        return null;
    }
}
