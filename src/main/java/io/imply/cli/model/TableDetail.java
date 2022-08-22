package io.imply.cli.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

public class TableDetail {

    public enum TablePartitioningGranularity{ hour, day, week, month, year }

    @Spec
    CommandSpec spec;

    @Option(names = {"-n","--name"}, description = "Table name", required = true)
    public String name;

    @Option(names = {"-v","--version"}, description = "Version number of the table", defaultValue = "0", required = true)
    public int version;

    @Option(names = {"-g","--granularity"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "day", required = true)
    public TablePartitioningGranularity partitioningGranularity;

    @Option(names = {"-c","--columns"}, description = "Cluster columns")
    public List<String> clusteringColumns;

    @Option(names = {"-d","--description"}, description = "Table description.")
    public String description;

    public Map<String, String> schemaMap;

    @Option(names = {"-S", "--schema"}, description = "Schema map")
    public void setSchemaMap(Map<String, String> schemaMap){
        Collection<String> values = schemaMap.values();
        for(String v: values){
            if(!"string".equals(v) && !"long".equals(v) && !"double".equals(v) && !"timestamp".equals(v)){
                throw new ParameterException(spec.commandLine(), "Invalid value for option " +
                        "'--schema' (<String=String>): expected one of [timestamp, string, long, double] (case-sensitive) but was '"+v+"'");
            }
        }
        this.schemaMap = schemaMap;
    }

    public String toJSONObject(){
        JSONObject obj = new JSONObject()
                .put("type", "detail")
                .put("name", name)
                .put("version", version)
                .put("partitioningGranularity", partitioningGranularity.name())
                .put("clusteringColumns", new JSONArray(clusteringColumns))
                .put("description", description)
                .put("schema", mapToJsonArray(schemaMap));
        return obj.toString();
    }

    @Override
    public String toString() {
        return "TableDetail{" +
                "name='" + name + '\'' +
                ", version=" + version +
                ", partitioningGranularity=" + partitioningGranularity +
                ", clusteringColumns=" + clusteringColumns +
                ", description='" + description + '\'' +
                ", schemaMap=" + schemaMap +
                '}';
    }

    private JSONArray mapToJsonArray(Map<String,String> map){
        JSONArray mapArray = new JSONArray();
        if(map == null){
            return mapArray;
        }
        Set<Map.Entry<String,String>> entrySet = map.entrySet();
        for(Map.Entry<String,String> entry: entrySet){
            String k = entry.getKey();
            String v = entry.getValue();
            mapArray.put(new JSONObject()
                    .put("name", k)
                    .put("dataType", v));
        }
        return mapArray;
    }

}
