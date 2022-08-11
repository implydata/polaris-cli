package io.imply.cli;

import io.imply.cli.model.Global;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(name = "datacube", description = "Manage data cubes",
        subcommandsRepeatable = true)
public class DatacubeCommand extends BaseCommand{

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    boolean help;

    @Command(name = "list", description = "list all datacubes")
    public void list(
            @Mixin Global settings) {
        System.out.println("datacubes list command");
    }

}
