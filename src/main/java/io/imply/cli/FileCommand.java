package io.imply.cli;

import io.imply.cli.model.Global;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;

@Command(name = "files", description = "Upload, list, and delete files(v1)",
        subcommandsRepeatable = true)
public class FileCommand extends BaseCommand{

    private static final String PATH = "/v1/files";

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    public boolean help;

    @Command(name = "list", description = "List all files")
    public void list(
            @Mixin Global settings) {
        try {
            String response = getRequest(PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "upload", description = "Upload a file")
    public void upload(
            @Option(names ={"-f", "--file"}, description = "File to upload", required = true) File file,
            @Mixin Global settings) {
        try {
            String response = upload(file, PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "get", description = "Get file details")
    public void get(
            @Option(names = {"-n","--name"}, description = "File Name", required = true)
                    String name,
            @Mixin Global settings) {
        try {
            String response = getRequest(PATH +"/" + name, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Get file details");
    }

    @Command(name = "delete", description = "Delete a file")
    public void delete(
            @Mixin Global settings) {
        System.out.println("Not supported yet");
    }
}
