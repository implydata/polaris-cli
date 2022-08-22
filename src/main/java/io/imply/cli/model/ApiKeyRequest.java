package io.imply.cli.model;

import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApiKeyRequest {

    @Spec
    CommandSpec spec;

    private static final Set<String> roleSet = new HashSet<>(Arrays.asList("ViewTables", "AccessQueries", "TestOnly"));

    @Option(names = {"-n","--name"}, description = "Api Key name", required = true)
    public String name;

    @Option(names = {"-r","--roles"}, description = "Role List")
    public List<String> roles;

//    @Option(names = {"-r","--roles"}, description = "Role List")
//    public void setRoles(List<String> roles){
//        for(String r: roles){
//            if(!roleSet.contains(r)){
//                throw new ParameterException(spec.commandLine(), "Invalid value for option " +
//                        "'--roles' (String): expected one of [ViewTables,AccessQueries,Others...] (case-sensitive) but was '"+r+"'");
//            }
//        }
//
//        this.roles = roles;
//    }

    @Option(names = {"-d","--description"}, description = "Table description.")
    public String description;

}
