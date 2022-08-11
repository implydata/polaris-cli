package io.imply.cli.model;

import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import java.util.*;

public class JobSourceV2 {

    @Spec
    CommandSpec spec;

    private Set<String> validFormat = new HashSet<>(Arrays.asList("nd-json","json","tsv","csv"));

    private Set<String> validType = new HashSet<>(Arrays.asList("string","long","double"));

    @Option(names = {"--files"}, description = "File List", required = true)
    public List<String> fileList;

    public String format;

    @Option(names = {"-f","--format"}, description = "Format, support nd-json,json,tsv,csv", defaultValue = "nd-json")
    public void setFormat(String format){
        if(!validFormat.contains(format)){
            throw new ParameterException(spec.commandLine(), "Invalid value for option " +
                    "'--format' (String): expected one of [nd-json,json,csv,tsv] (case-sensitive) but was '"+format+"'");
        }
        this.format = format;
    }

    public Map<String, String> inputSchema;

    @Option(names = {"-S", "--inputSchema"}, description = "InputSchema (E.g.,-S_time=string -Srevenue=long)")
    public void setInputSchema(Map<String,String> inputSchema){
        Collection<String> types = inputSchema.values();
        for(String t: types){
            if(!validType.contains(t)){
                throw new ParameterException(spec.commandLine(), "Invalid value for option " +
                        "'--inputSchema' (<String=String>): expected one of [string, long, double] (case-sensitive) but was '"+t+"'");

            }
        }
        this.inputSchema = inputSchema;
    }

    @Override
    public String toString() {
        return "JobSourceV2{" +
                "fileList=" + fileList +
                ", format='" + format + '\'' +
                ", inputSchema=" + inputSchema +
                '}';
    }
}
