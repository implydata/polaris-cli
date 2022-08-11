package io.imply.cli;

import io.imply.cli.model.Global;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(name = "connection", description = "Manage connections to external sources of data",
        subcommandsRepeatable = true)
public class ConnectionCommand extends BaseCommand{

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    boolean help;

    @Command(name = "list", description = "List all connections")
    public void list(
            @Mixin Global settings) {
        System.out.println("List all connections");
    }

    @Command(name = "confluent", description = "Create a confluent connection")
    public void createConfluent(
            @Mixin Global settings) {
        System.out.println("sources list command");
    }

    @Command(name = "push_streaming", description = "Create a push_streaming connection")
    public void createPushStreaming(
            @Mixin Global settings) {
        System.out.println("sources list command");
    }

    @Command(name = "get", description = "Get connection details")
    public void get(
            @Mixin Global settings) {
        System.out.println("Get connection details");
    }

    @Command(name = "update", description = "Update a connection")
    public void update(
            @Mixin Global settings) {
        System.out.println("Update a connection");
    }

    @Command(name = "delete", description = "Delete a connection")
    public void delete(
            @Mixin Global settings) {
        System.out.println("Delete a connection");
    }

    @Command(name = "secret", description = "Update connection secrets")
    public void secret(
            @Mixin Global settings) {
        System.out.println("Update connection secrets");
    }

    @Command(name = "test", description = "Test a connection")
    public void test(
            @Mixin Global settings) {
        System.out.println("Test a connection");
    }

}
