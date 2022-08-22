package io.imply.cli;

import io.imply.cli.model.ApiKeyRequest;
import io.imply.cli.model.Global;
import org.json.JSONObject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Mixin;

import java.io.IOException;

@Command(name = "apikeys", description = "Manage Api Keys")
public class ApiKeyCommand extends BaseCommand {

    private static final String PATH = "/apikeys";

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "Display this help and exit")
    public boolean help;

    @Command(name = "create", description = "create a api key")
    public void create(
            @Mixin ApiKeyRequest apiKeyRequest,
            @Mixin Global settings) {
        JSONObject obj = new JSONObject().put("name", apiKeyRequest.name)
                .put("description", apiKeyRequest.description)
                .put("roles", apiKeyRequest.roles);
        try {
            String response = postJson(obj.toString(), PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "list", description = "List all api keys")
    public void list(
            @Mixin Global settings) {
        try {
            String response = getRequest(PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "get", description = "Get an API Key")
    public void get(
            @Option(names = {"--id"}, description = "API Key ID", required = true)
                    String id,
            @Mixin Global settings) {
        try {
            String response = getRequest(PATH + "/" + id, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "delete", description = "Delete an API Key")
    public void delete(
            @Option(names = {"--id"}, description = "API Key ID", required = true)
                    String id,
            @Mixin Global settings) {
        try {
            deleteRequest(PATH + "/" + id, settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "patch", description = "Update/Patch an API Key")
    public void patch(
            @Option(names = {"--id"}, description = "API Key ID", required = true)
                    String id,
            @Mixin ApiKeyRequest apiKeyRequest,
            @Mixin Global settings) {
        try {
            JSONObject obj = new JSONObject().put("name", apiKeyRequest.name)
                    .put("description", apiKeyRequest.description)
                    .put("roles", apiKeyRequest.roles);
            String response = patchRequest(obj.toString(), PATH + "/" + id, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
