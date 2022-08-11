package io.imply.cli;

import io.imply.cli.model.Global;
import io.imply.cli.model.TableDetail;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.io.IOException;

@Command(name = "table", description = "Manage tables and their schemas", sortOptions = false)
public class TableCommand extends BaseCommand {

    private static final String PATH = "/v2/tables";

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "Display this help and exit")
    boolean help;

    @Command(name = "list", description = "List all tables")
    public void list(
            @Mixin Global settings) {
        String response;
        try {
            response = getRequest(PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "detail", description = "Create a detail table")
    public void createDetail(
            @Mixin Global settings,
            @Mixin TableDetail tableDetail) {
        try {
            String response = postJson(tableDetail.toJSONObject(), PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "aggregate", description = "Create an aggregate table")
    public void createAggregate(
            @Mixin Global settings) {
        System.out.println("table create aggregate command");
    }

    @Command(name = "get", description = "Get a table detail (todo)")
    public void get(
            @Option(names = {"-n","--tableName"}, description = "Table Name", required = true)
                    String tableName,
            @Mixin Global settings) {
        try {
            String response = getRequest(PATH +"/" + tableName, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "update", description = "Update a table")
    public void update(
            @Mixin Global settings) {
        System.out.println("table update command");
    }

}
