package nl.knaw.dans.ingest.core.legacy;

import nl.knaw.dans.easy.dd2d.DepositIngestTask;
import nl.knaw.dans.ingest.core.Deposit;
import org.apache.commons.configuration.ConfigurationException;

import java.io.IOException;
import java.nio.file.Path;

public class DepositIngestTaskWrapper  {
    private final DepositIngestTask task;

    public DepositIngestTaskWrapper(DepositIngestTask task) throws IOException, ConfigurationException {
        this.task = task;
    }

    public String getDoi() {
        return task.deposit().doi();
    }

}
