package info.kucharczyk.java.simple_people_base.cli;

import org.apache.commons.cli.*;

public class AppUserInterface {
    private Options options;
    private UserInputs userInputs;

    public AppUserInterface() {
        options = new Options();
        userInputs = null;
    }

    public void init() {
        options.addRequiredOption("i", "input", true, "input file to parse");
        options.addOption("t", "type", true, "input file type, options: csv/xml");
        options.addRequiredOption("cs", "connection-string", true, "connection string, example: jdbc:h2:./base");
        options.addOption("h", "help", false, "help");
    }

    private void displayHelp() {
        String header = "Transfer useful data from the source file to the database\n\n";

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("spb", header, options, null, true);
    }

    private UserInputs readInputs(CommandLine cmd) {
        UserInputs result = new UserInputs();

        result.setInputPath(cmd.getOptionValue("i"));
        result.setType(cmd.getOptionValue("t"));
        result.setConnectionString(cmd.getOptionValue("cs"));

        return result;
    }

    public boolean parse(String args[]) {
        boolean displayHelp = false;

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            displayHelp = cmd.hasOption("h");
            userInputs = readInputs(cmd);
        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            displayHelp = true;
        }

        if (displayHelp) {
            displayHelp();
            return false;
        }
        return true;
    }

    public UserInputs getinputs() {
        return userInputs;
    }
}