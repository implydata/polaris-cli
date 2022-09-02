package io.imply.cli;

import io.imply.cli.model.Global;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(name = "connections", description = "Manage connections to external sources of data(v2)",
        subcommandsRepeatable = true)
public class ConnectionCommand extends BaseCommand{

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    boolean help;

    @Command(name = "list", description = "List all connections")
    public void list(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "confluent", description = "Create a confluent connection")
    public void createConfluent(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "push_streaming", description = "Create a push_streaming connection")
    public void createPushStreaming(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "get", description = "Get connection details")
    public void get(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "update", description = "Update a connection")
    public void update(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "delete", description = "Delete a connection")
    public void delete(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "secret", description = "Update connection secrets")
    public void secret(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "test", description = "Test a connection")
    public void test(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

}
