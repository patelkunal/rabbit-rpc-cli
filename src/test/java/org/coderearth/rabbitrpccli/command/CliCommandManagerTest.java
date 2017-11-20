package org.coderearth.rabbitrpccli.command;

import org.apache.commons.cli.*;
import org.junit.Test;

/**
 * Created by kunal_patel on 20/11/17.
 */
public class CliCommandManagerTest {

    @Test
    public void test() throws ParseException {
        final CliCommandManager cliCommandManager = new CliCommandManager();
        cliCommandManager.init();

        final String[] args = {"--exchange=foo.exchange", "--routing_key=foo.queue", "--operation=GET", "-H"};
        cliCommandManager.parse(args);
    }


    @Test
    public void test2() {
        final Options options = new Options();
        final HelpFormatter helpFormatter = new HelpFormatter();
        options.addOption(
                Option.builder("H").argName("H").longOpt("header").hasArgs().valueSeparator().build()
        );
        final DefaultParser parser = new DefaultParser();

        final String[] args = {"-Hfoo=FOO", "-Hbar=BAR", "-Hbaz=BAZ"};

        try {
            final CommandLine cmd = parser.parse(options, args, false);
            System.out.println(cmd.getOptionProperties("H"));
        } catch (ParseException pe) {
            helpFormatter.printHelp("foo", options);
        }
    }

}