package io.imply.cli;

import io.imply.cli.model.Global;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

@Command(name = "users", description = "Manage user and it's access to protected data and resources",
        subcommandsRepeatable = true)
public class UserCommand extends BaseCommand{

    @CommandLine.Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    boolean help;

    @Command(name = "list", description = "list all users")
    public void list(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

}
