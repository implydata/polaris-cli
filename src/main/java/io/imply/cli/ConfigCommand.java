package io.imply.cli;

import io.imply.cli.model.Global;
import org.apache.hc.client5.http.ConnectTimeoutException;
import org.json.JSONObject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

@Command(name = "config",
        description = "Obtain an API token and save it for later use",
        sortOptions = false)
public class ConfigCommand extends BaseCommand implements Runnable{

    @Spec
    private CommandSpec spec;
    private static final String PATH = "/protocol/openid-connect/token";
    private final String home = System.getProperty("user.home");
    private final File file = new File(home, PolarisMainCli.POLARIS_FILE);


    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help and exit")
    public boolean help;

    @Option(names = {"-e","--environment"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "${IMPLY_ENV}", required = true)
    Global.Environment environment;

    @Option(names = {"-o","--organization"}, description = "Name of your organization in Polaris",
            defaultValue = "${IMPLY_ORG}", required = true)
    String organization;

    @ArgGroup(heading = "Token section%n", exclusive = false)
    TokenSection tokenSection;

    @ArgGroup(validate = false, heading = "Api key section%n")
    ApikeySection apikeySection;

    @Option(names = {"--auth"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "${IMPLY_AUTHORIZATION}")
    public Global.Authorization authorization;

    static class TokenSection{
        @Option(names = {"--client_id"}, description = "The name of the client you configured",
                defaultValue = "${IMPLY_CLIENT_ID}", required = true)
        String client_id;

        @Option(names = {"--client_secret"}, description = "API Client Secret generated for the client",
                defaultValue = "${IMPLY_CLIENT_SECRET}", required = true)
        String client_secret;
    }

    static class ApikeySection{
        @Option(names = {"-k", "--apiKey"}, description = "The apiKey to a Polaris API")
        public String apiKey;
    }

    @Option(names= {"--verbose"}, description = "Enable to print debug info")
    public boolean verbose;

    @Override
    public void run() {
        // retrieve token from POLARIS
        try {
            JSONObject obj = read(file);
            if(apikeySection != null){
                obj.put("IMPLY_APIKEY", apikeySection.apiKey);
            }

            if(tokenSection != null){
                String token = retrieveToken(
                        environment.name(), organization,
                        PATH,
                        tokenSection.client_id, tokenSection.client_secret, verbose);
                obj.put("IMPLY_ENV", environment)
                        .put("IMPLY_ORG", organization)
                        .put("IMPLY_TOKEN", token);
            }

            if(authorization == Global.Authorization.token && !obj.has("IMPLY_TOKEN")) {
                throw new ParameterException(spec.commandLine(), "client_id/client_secret is required");
            }

            if(authorization == Global.Authorization.basic && !obj.has("IMPLY_APIKEY")) {
                throw new ParameterException(spec.commandLine(), "API Key is required");
            }
            obj.put("IMPLY_AUTHORIZATION", authorization.name());
            write(file, obj);

            System.out.println(obj);
        } catch (IOException e) {
            if ( e instanceof ConnectTimeoutException){
                System.out.println(e.getMessage());
            }else{
                e.printStackTrace();
            }
        }
    }

    private JSONObject read(File file) throws IOException {
        if(file.exists()){
            byte[] bytes = Files.readAllBytes(file.toPath());
            String s = new String(bytes);
            return new JSONObject(s);
        }
        return new JSONObject();
    }

    private void write(File file, JSONObject obj) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(file)){
            out.println(obj.toString(2));
        }
    }
}
