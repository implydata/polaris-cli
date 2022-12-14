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

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Command(name = "jobs", description = "Manage ingestion and deletion jobs in Polaris(v2)")
public class JobCommand extends BaseCommand{

    private static final String PATH = "/v2/jobs";

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    boolean help;

    @Command(name = "list", descriptionHeading = "Description:%n", description = "List all jobs",
            optionListHeading = "%n")
    public void list(
            @Mixin Global settings) {
        try {
            String response = getRequest(PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "get", descriptionHeading = "Description:%n", description = "Get job details",
            optionListHeading = "%nJobs option:%n")
    public void get(
            @Option(names = {"-j","--jobId"}, description = "Job Id", required = true)
                    String jobId,
            @Mixin Global settings) {
        try {
            String response = getRequest(PATH +"/" + jobId, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "update", descriptionHeading = "Description:%n", description = "Update a job",
            optionListHeading = "%nJobs option:%n")
    public void update(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "batch", descriptionHeading = "Description:%n", description = "Create batch ingestion job",
            optionListHeading = "%nJobs option:%n")
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
        try {
            String response = postJson(obj.toString(), PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "drop_table", descriptionHeading = "Description:%n",
            description = "Create job to delete a table and all of its data", optionListHeading = "%nJobs option:%n")
    public void createDropTable(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "delete_data", descriptionHeading = "Description:%n",
            description = "Create job to delete data within a table", optionListHeading = "%nJobs option:%n")
    public void createDeleteData(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }

    @Command(name = "streaming", descriptionHeading = "Description:%n",
            description = "Create streaming ingestion job", optionListHeading = "%nJobs option:%n")
    public void createStreaming(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
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
