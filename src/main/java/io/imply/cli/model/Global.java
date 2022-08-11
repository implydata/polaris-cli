package io.imply.cli.model;

import picocli.CommandLine.Option;

public class Global {

    public enum Environment { eng, staging, prod }

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "Display this help and exit")
    boolean help;

    @Option(names = {"-e","--environment"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "${IMPLY_ENV}", required = true)
    public Environment environment;

    @Option(names = {"-o","--organization"}, description = "Organization name",
            defaultValue = "${IMPLY_ORG}", required = true)
    public String organization;

    @Option(names = {"-t", "--token"}, description = "The access token to a Polaris API",
            defaultValue = "${IMPLY_TOKEN}", required = true)
    public String token;

    @Option(names= {"--verbose"}, description = "Enable to print debug info")
    public boolean verbose;

}
