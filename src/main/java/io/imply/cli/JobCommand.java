package io.imply.cli;

import io.imply.cli.model.Global;
import io.imply.cli.model.InputToTableSchemaMappingsV2;
import io.imply.cli.model.JobSourceV2;
import io.imply.cli.model.JobTargetV2;
import org.json.JSONArray;
import org.json.JSONObject;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Command(name = "job", description = "Manage jobs in Imply Polaris")
public class JobCommand extends BaseCommand{

    private static final String PATH = "/v2/jobs";

    @Spec
    CommandSpec spec;

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    boolean help;

    @Command(name = "list", description = "List all jobs")
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

    @Command(name = "get", description = "Get job details")
    public void get(
            @Option(names = {"-j","--jobId"}, description = "Job Id", required = true)
                    String jobId,
            @Mixin Global settings) {
        String response;
        try {
            response = getRequest(PATH +"/" + jobId, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "update", description = "Update a job")
    public void update(
            @Mixin Global settings) {
        System.out.println("Update a job");
    }

    @Command(name = "batch", description = "Create batch ingestion job")
    public void createBatch(
            @Mixin JobSourceV2 jobSourceV2,
            @Mixin JobTargetV2 jobTargetV2,
            @Mixin InputToTableSchemaMappingsV2 inputToTableSchemaMappingsV2,
            @Mixin Global settings) {
        JSONObject obj = new JSONObject()
                .put("type", "batch")
                .put("ingestionMode", jobTargetV2.ingestionMode.name())
                .put("source", new JSONObject()
                        .put("type", "uploaded")
                        .put("fileList", new JSONArray(jobSourceV2.fileList))
                        .put("formatSettings", new JSONObject().put("format", jobSourceV2.format))
                        .put("inputSchema", mapToJsonArray(jobSourceV2.inputSchema, "name", "dataType")))
                .put("target", new JSONObject()
                        .put("type", "table")
                        .put("tableName", jobTargetV2.tableName)
                        .put("intervals", new JSONArray()))
                .put("mappings", mapToJsonArray(inputToTableSchemaMappingsV2.mappings, "columnName", "expression"));
        String response;
        try {
            response = postJson(obj.toString(), PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "drop_table", description = "Create job to delete a table and all of its data")
    public void createDropTable(
            @Mixin Global settings) {
        System.out.println("drop table....");
    }

    @Command(name = "delete_data", description = "Create job to delete data within a table")
    public void createDeleteData(
            @Mixin Global settings) {
        System.out.println("delete data");
    }

    @Command(name = "streaming", description = "Create streaming ingestion job")
    public void createStreaming(
            @Mixin Global settings) {
        System.out.println("streaming");
    }

    private JSONArray mapToJsonArray(Map<String,String> map, String k1, String k2){
        JSONArray mapArray = new JSONArray();
        if(map == null){
            return mapArray;
        }
        Set<Entry<String,String>> entrySet = map.entrySet();
        for(Entry<String,String> entry: entrySet){
            String k = entry.getKey();
            String v = entry.getValue();
            if("expression".equals(k2)){
                if("_time".equals(k)){
                    k = "__time";
                    v = expressionFormat(v);
                }else{
                    v = "\"" + v + "\"";
                }
            }
            mapArray.put(new JSONObject()
                    .put(k1, k)
                    .put(k2, v));
        }
        return mapArray;
    }

    private String expressionFormat(String format){
        if("AUTO".equals(format) || "ISO".equals(format)){
            return "TIME_PARSE(\"_time\")";
        }else if("POSIXUTC".equals(format)){
            return "MILLIS_TO_TIMESTAMP(\"_time\" * 1000)";
        }else if("MILLISUTC".equals(format)){
            return "MILLIS_TO_TIMESTAMP(\"_time\")";
        }else if("MICROUTC".equals(format)){
            return "MILLIS_TO_TIMESTAMP(\"_time\" / 1000)";
        }else if("NANOUTC".equals(format)) {
            return "MILLIS_TO_TIMESTAMP(\"_time\" / 1000000)";
        }
        throw new RuntimeException("Unknown format: " + format);
    }

}
