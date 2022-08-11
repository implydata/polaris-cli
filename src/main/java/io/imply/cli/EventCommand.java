package io.imply.cli;

import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

@Command(name = "event",
        description = "Push events to a Polaris connection")
public class EventCommand extends BaseCommand implements Runnable{

    @Option(names = {"-p","--payload"}, description = "Data to be pushed to the table in Polaris in " +
            "newline-delimited JSON format. The JSON body must include a timestamp as a key-value pair.",
            required = true)
    String payload;

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    boolean help;

    @Override
    public void run() {

    }
}
