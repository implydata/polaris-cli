package io.imply.cli;

import org.json.JSONObject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import java.io.File;
import java.nio.file.Files;
import java.util.Set;

@Command(name = "polaris", subcommands = {
        ConfigCommand.class,
        TableCommand.class,
        FileCommand.class,
        ConnectionCommand.class,
        EventCommand.class,
        JobCommand.class,
        SqlCommand.class,
        ApiKeyCommand.class,
        DatacubeCommand.class,
        DashboardCommand.class,
        UserCommand.class,
        ProjectCommand.class})
public class PolarisMainCli implements Runnable{

    public final static String POLARIS_FILE = ".imply_polaris";

    public PolarisMainCli(){
        String home = System.getProperty("user.home");
        File file = new File(home, POLARIS_FILE);
        if(file.exists()){
            try {
                byte[] data = Files.readAllBytes(file.toPath());
                String content = new String(data);
                JSONObject obj = new JSONObject(content);
                Set<String> keySet = obj.keySet();
                for(String k: keySet){
                    System.setProperty(k, obj.getString(k));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Spec
    CommandSpec spec;

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "Display this help and exit")
    public boolean help;

    public static void main(String[] args){
        CommandLine cli = new CommandLine(new PolarisMainCli());
        cli.execute(args);
    }

    @Override
    public void run() {
//        spec.commandLine().usage(System.err);
        throw new ParameterException(spec.commandLine(), "Specify a subcommand");
    }
}
