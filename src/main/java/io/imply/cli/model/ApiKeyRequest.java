package io.imply.cli.model;

import picocli.CommandLine.Option;
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

    @Option(names = {"-d","--description"}, description = "Table description.")
    public String description;

}
