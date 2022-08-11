package io.imply.cli;

import io.imply.cli.model.Global;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(name = "project", description = "Manage Imply Polaris resources",
        subcommandsRepeatable = true)
public class ProjectCommand extends BaseCommand {

    @CommandLine.Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    boolean help;

    @Command(name = "list", description = "List all project plans")
    public void list(
            @Mixin Global settings) {
        System.out.println("List project plans");
    }

    @Command(name = "update", description = "Update a project plan")
    public void update(
            @Mixin Global settings) {
        System.out.println("Update project plan");
    }
}
