package io.imply.cli.model;

import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import java.util.*;

public class InputToTableSchemaMappingsV2 {

    private Set<String> validType = new HashSet<>(Arrays.asList("AUTO","ISO","POSIXUTC","MILLISUTC","MICROUTC","NANOUTC"));

    @Spec
    CommandSpec spec;

    public Map<String, String> mappings;

    @Option(names = {"-M", "--mappings"}, description = "InputSchema (E.g.,-S_time=AUTO -Scountry=country)")
    public void setMappings(Map<String,String> mappings){
        Set<String> keys = mappings.keySet();
        for(String k: keys){
            if("__time".equals(k)){
                String v = mappings.get(k);
                if(!validType.contains(v)){
                    throw new ParameterException(spec.commandLine(), "Invalid value for option " +
                            "'--inputSchema' (<String=String>): expected one of [AUTO,ISO,POSIXUTC,MILLISUTC,MICROUTC,NANOUTC] (case-sensitive) but was '"+v+"'");

                }
            }

        }
        this.mappings = mappings;
    }

    @Override
    public String toString() {
        return "InputToTableSchemaMappingsV2{" +
                "mappings=" + mappings +
                '}';
    }
}
