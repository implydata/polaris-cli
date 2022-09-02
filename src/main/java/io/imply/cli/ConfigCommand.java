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
        description = "Config CLI for env,org,token and apKey",
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

    @ArgGroup
    TokenAPIGroup tokenAPIGroup;

    @Option(names = {"-k", "--apiKey"}, description = "The apiKey to a Polaris API", defaultValue = "${IMPLY_APIKEY}")
    public String apiKey;

    @Option(names = {"--auth"}, description = "Enum values: ${COMPLETION-CANDIDATES}",
            defaultValue = "${IMPLY_AUTHORIZATION}", required = true)
    public Global.Authorization authorization;

    static class TokenAPIGroup{
        @ArgGroup(heading = "Client ID/Secret section%n", exclusive = false)
        ClientIdSection clientSection;

        @Option(names = {"-t", "--token"}, description = "Set an user or client token", required = true)
        public String token;
    }

    static class ClientIdSection {
        @Option(names = {"--client_id"}, description = "The name of the client you configured", required = true)
        String client_id;

        @Option(names = {"--client_secret"}, description = "API Client Secret generated for the client", required = true)
        String client_secret;
    }

    @Option(names= {"--verbose"}, description = "Enable to print debug info")
    public boolean verbose;

    @Override
    public void run() {
        // retrieve token from POLARIS
        try {
            JSONObject obj = read(file);
            obj.put("IMPLY_ENV", environment)
                    .put("IMPLY_ORG", organization);

            if(apiKey != null){
                obj.put("IMPLY_APIKEY", apiKey);
            }

            if(tokenAPIGroup!=null && tokenAPIGroup.clientSection != null){
                String token = retrieveToken(
                        environment.name(), organization,
                        PATH,
                        tokenAPIGroup.clientSection.client_id, tokenAPIGroup.clientSection.client_secret, verbose);
                obj.put("IMPLY_TOKEN", token);
            }

            if(tokenAPIGroup!=null && tokenAPIGroup.token != null){
                obj.put("IMPLY_TOKEN", tokenAPIGroup.token);
            }

            if(authorization == Global.Authorization.token && !obj.has("IMPLY_TOKEN")) {
                throw new ParameterException(spec.commandLine(), "client_id/client_secret, or token is required");
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
