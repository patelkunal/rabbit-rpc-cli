package org.coderearth.rabbitrpccli.command;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CliCommandManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CliCommandManager.class);

    private final Options options;
    private final HelpFormatter helpFormatter;
    private final CommandLineParser commandLineParser;

    public CliCommandManager() {
        options = new Options();
        helpFormatter = new HelpFormatter();
        commandLineParser = new DefaultParser();
    }

    public Command parse(String[] args) throws ParseException {
        final CommandLine cmd = commandLineParser.parse(options, args, false);
        LOGGER.debug("{}", cmd.getOptionValue("exchange"));
        LOGGER.debug("{}", cmd.getOptionValue("queue"));
        LOGGER.debug("{}", cmd.getOptionValue("operation"));
        LOGGER.debug("{}", cmd.getOptionProperties("H"));
        return Command
                .builder()
                .exchange(cmd.getOptionValue("exchange"))
                .queue(cmd.getOptionValue("queue"))
                .operation(cmd.getOptionValue("operation"))
                .headers(cmd.getOptionProperties("H"))
                .build();
    }

    @PostConstruct
    public void init() {
        options.addOption(
                Option
                        .builder("E")
                        .longOpt("exchange")
                        .desc("rabbitmq exchange on which message has to be published")
                        .hasArg(true)
                        .numberOfArgs(1)
                        .required(true)
                        .type(String.class)
                        .build()
        );
        options.addOption(
                Option
                        .builder("Q")
                        .longOpt("queue")
                        .desc("rabbitmq queue on which messaged should be pushed")
                        .hasArg(true)
                        .numberOfArgs(1)
                        .required(true)
                        .type(String.class)
                        .build()
        );
        options.addOption(
                Option
                        .builder("O")
                        .longOpt("operation")
                        .desc("operation (GET/POST/PUT/DELETE) to be used in header (x-rest-verb) to the message")
                        .hasArg(true)
                        .numberOfArgs(1)
                        .required(true)
                        .type(String.class)
                        .build()
        );
        options.addOption(
                Option
                        .builder("H")
                        .longOpt("header")
                        .hasArgs()
                        .valueSeparator()
                        .desc("custom headers which will be published along with message")
                        .required(false)
                        .build()
        );
    }
}
