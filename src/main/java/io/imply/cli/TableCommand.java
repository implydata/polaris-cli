package io.imply.cli;

import io.imply.cli.model.Global;
import io.imply.cli.model.TableDetail;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.io.IOException;

@Command(name = "tables", description = "Create, view, and update tables(v2)", sortOptions = false)
public class TableCommand extends BaseCommand {

    private static final String PATH = "/v2/tables";

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "Display this help and exit")
    public boolean help;

    @Command(name = "list", description = "List all tables", descriptionHeading = "Description:%n", optionListHeading = "Options:%n")
    public void list(
            @Mixin Global settings) {
        try {
            String resp = getRequest(PATH, settings);
            print(settings, resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "detail", descriptionHeading = "Description:%n", optionListHeading = "Options:%n",
            description = "Create a detail table")
    public void createDetail(
            @Mixin Global settings,
            @Mixin TableDetail tableDetail) {
        try {
            String resp = postJson(tableDetail.toJSONObject(), PATH, settings);
            print(settings, resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "aggregate", description = "Create an aggregate table")
    public void createAggregate(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "get", description = "Get a table detail")
    public void get(
            @Option(names = {"-n","--tableName"}, description = "Table Name",
                    required = true, paramLabel = "<tableName>")
                    String tableName,
            @Mixin Global settings) {
        try {
            String resp = getRequest(PATH +"/" + tableName, settings);
            print(settings, resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "update", description = "Update a table")
    public void update(
            @Mixin TableDetail tableDetail,
            @Mixin Global settings) {
        try {
            String resp = putJSON(tableDetail.toJSONObject(), PATH + "/" + tableDetail.name, settings);
            print(settings, resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
