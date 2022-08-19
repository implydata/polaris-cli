package io.imply.cli;

import io.imply.cli.model.Global;
import org.apache.hc.client5.http.ConnectTimeoutException;
import org.json.JSONObject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@Command(name = "login",
        description = "Obtain an API token and save it for later use",
        sortOptions = false)
public class LoginCommand extends BaseCommand implements Runnable{

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

    @Option(names = {"--client_id"}, description = "The name of the client you configured",
            defaultValue = "${IMPLY_CLIENT_ID}", required = true)
    String client_id;

    @Option(names = {"--client_secret"}, description = "API Client Secret generated for the client",
            defaultValue = "${IMPLY_CLIENT_SECRET}", required = true)
    String client_secret;

    @Option(names= {"--verbose"}, description = "Enable to print debug info")
    public boolean verbose;

    @Override
    public void run() {
        String home = System.getProperty("user.home");
        File file = new File(home, PolarisMainCli.POLARIS_FILE);
        // retrieve token from POLARIS
        String token;
        try {
            token = retrieveToken(environment.name(), organization, client_id, client_secret, verbose);
            try (PrintWriter out = new PrintWriter(file)){
                JSONObject obj = new JSONObject()
                        .put("IMPLY_ENV", environment)
                        .put("IMPLY_ORG", organization)
                        .put("IMPLY_TOKEN", token);
                out.println(obj.toString(2));

                System.out.println("Logged in!");
            }
        } catch (IOException e) {
            if ( e instanceof ConnectTimeoutException){
                System.out.println(e.getMessage());
            }else{
                e.printStackTrace();
            }
        }


    }
}
