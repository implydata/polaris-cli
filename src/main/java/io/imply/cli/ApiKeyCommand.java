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

    @Command(name = "create", descriptionHeading = "Description:%n",description = "Create an api key",
            optionListHeading = "%nApiKeys options:%n")
    public void create(
            @Mixin ApiKeyRequest apiKeyRequest,
            @Mixin Global settings) {
        JSONObject obj = new JSONObject().put("name", apiKeyRequest.name)
                .put("description", apiKeyRequest.description)
                .put("permissions", apiKeyRequest.roles);
        try {
            String response = postJson(obj.toString(), PATH, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "list", descriptionHeading = "Description:%n", description = "List all api keys",
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

    @Command(name = "get", descriptionHeading = "Description:%n", description = "Get an API Key",
            optionListHeading = "%nApiKeys options:%n")
    public void get(
            @Option(names = {"--id"}, description = "API Key ID", required = true, paramLabel="<id>")
                    String id,
            @Mixin Global settings) {
        try {
            String response = getRequest(PATH + "/" + id, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "delete", descriptionHeading = "Description:%n", description = "Delete an API Key",
            optionListHeading = "%nApiKeys options:%n")
    public void delete(
            @Option(names = {"--id"}, description = "API Key ID", required = true, paramLabel="<id>")
                    String id,
            @Mixin Global settings) {
        try {
            deleteRequest(PATH + "/" + id, settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "patch", descriptionHeading = "Description:%n", description = "Update an API Key",
            optionListHeading = "%nApiKeys options:%n")
    public void patch(
            @Option(names = {"--id"}, description = "API Key ID", required = true)
                    String id,
            @Mixin ApiKeyRequest apiKeyRequest,
            @Mixin Global settings) {
        try {
            JSONObject obj = new JSONObject()
                    .put("name", apiKeyRequest.name)
                    .put("description", apiKeyRequest.description)
                    .put("permissions", apiKeyRequest.roles);
            String response = patchRequest(obj.toString(), PATH + "/" + id, settings);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
