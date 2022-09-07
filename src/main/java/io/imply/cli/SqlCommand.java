package io.imply.cli;

import io.imply.cli.model.Global;
import org.json.JSONObject;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.util.UUID;

@Command(name = "sql",
        description = "Issue SQL queries against Polaris tables(v1)")
public class SqlCommand extends BaseCommand implements Runnable{

    private static final String path = "/v1/query/sql";

    @Mixin
    Global settings;

    @Option(names = {"-q","--query"}, description = "Sql Query", required = true)
    String query;

    @Override
    public void run() {
        JSONObject queryObj = new JSONObject()
                .put("query", query)
                .put("context", new JSONObject()
                        .put("sqlOuterLimit", 1001)
                        .put("sqlQueryId", UUID.randomUUID().toString()))
                .put("header", true)
                .put("resultFormat", "array")
                .put("sqlTypesHeader", true)
                .put("typesHeader", true);
        try {
            String resp = postJson(queryObj.toString(), path, settings);
            print(settings, resp);
        } catch (IOException e) {
            System.out.println("Error happens: " + e.getMessage());
        }

    }
}
