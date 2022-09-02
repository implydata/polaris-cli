package io.imply.cli.model;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

public class Global {

    public enum Environment { eng, staging, prod }
    public enum Authorization { token,  basic }

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

    @ArgGroup(heading = "Select token or API Key as auth method%n")
    public AuthSection authSection = new AuthSection();

    public static class AuthSection{
        @Option(names = {"-t", "--token"}, description = "The access token to a Polaris API",
                defaultValue = "${IMPLY_TOKEN}")
        public String token;

        @Option(names = {"-k", "--apiKey"}, description = "The apiKey to a Polaris API",
                defaultValue = "${IMPLY_APIKEY}")
        public String apiKey;
    }

    @Option(names = {"-a","--authorization"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "${IMPLY_AUTHORIZATION}")
    public Authorization authorization;

    @Option(names= {"--verbose"}, description = "Enable to print debug info")
    public boolean verbose;

}
