package io.imply.cli;

import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

@Command(name = "events",
        description = "Load data from an external data stream(v1)")
public class EventCommand extends BaseCommand implements Runnable{

    @Option(names = {"-p","--payload"}, description = "Data to be pushed to the table in Polaris in " +
            "newline-delimited JSON format",
            required = true)
    String payload;

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    boolean help;

    @Override
    public void run() {
        System.out.println("Not supported yet");
    }
}
