package io.imply.cli.model;

import picocli.CommandLine;
import picocli.CommandLine.Option;

public class Global {

    public enum Environment { eng, staging, prod }
    public enum Authorization { token,  basic }
    public enum Output { json,  table }

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "Display this help and exit")
    public boolean help;

    @Option(names = {"-e","--environment"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "${IMPLY_ENV}", required = true)
    public Environment environment;

    @Option(names = {"-o","--organization"}, description = "Organization name",
            defaultValue = "${IMPLY_ORG}", required = true)
    public String organization;

    @Option(names= {"--verbose"}, description = "Enable to print debug info")
    public boolean verbose;

    @Option(names = {"--output"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "json")
    public Output output = Output.json;

    @Option(names = {"-a","--authorization"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "${IMPLY_AUTHORIZATION}")
    public Authorization authorization = Authorization.token;

    @Option(names = {"-t", "--token"}, description = "The access token to a Polaris API",
            defaultValue = "${IMPLY_TOKEN}", required = true)
    public String token;

    @Option(names = {"-k", "--apiKey"}, description = "The apiKey to a Polaris API",
            defaultValue = "${IMPLY_APIKEY}")
    public String apiKey;

}
