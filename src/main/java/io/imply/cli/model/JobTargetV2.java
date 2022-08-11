package io.imply.cli.model;

import picocli.CommandLine.Option;

public class JobTargetV2 {

    public enum IngestionMode { append, replace };

    @Option(names = {"--ingestionMode"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "append", required = true)
    public IngestionMode ingestionMode;

    @Option(names = {"-n","--tableName"}, description = "Table name", required = true)
    public String tableName;

    @Override
    public String toString() {
        return "JobTargetV2{" +
                "ingestionMode=" + ingestionMode +
                ", tableName='" + tableName + '\'' +
                '}';
    }

}
