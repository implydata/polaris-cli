package io.imply.cli.model;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Model.CommandSpec;

public class Global {

    @Spec
    CommandSpec spec;

    public enum Environment { eng, staging, prod }
    public enum Authorization { token,  basic }
    public enum Output { json,  table }

    @ArgGroup(heading = "General options:%n", exclusive = false)
    public GeneralSection generalSection = new GeneralSection();

    // walk around
    public void validate() {
        if (generalSection.environment == null && generalSection.organization == null
                && generalSection.token == null) {
            throw new CommandLine.ParameterException(spec.commandLine(),
                    "Error: Missing required argument(s): --environment=<environment>, " +
                            "--organization=<organization>, --token=<token>");
        }
    }

    public static class GeneralSection{
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


}
