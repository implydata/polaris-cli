package io.imply.cli;

import io.imply.cli.model.Global;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;

@Command(name = "dashboard", description = "Manage dashboards",
        subcommandsRepeatable = true)
public class DashboardCommand extends BaseCommand{

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    public boolean help;

    @Command(name = "list", description = "list all dashboards")
    public void list(
            @Mixin Global settings) {
        System.out.println("dashboards list command");
    }
}
