package org.coderearth.rabbitrpccli;

import org.coderearth.rabbitrpccli.command.CliCommandManager;
import org.coderearth.rabbitrpccli.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Properties;
import java.util.UUID;

@Component
public class RabbitRpcCliRunner implements CommandLineRunner, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitRpcCliRunner.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CliCommandManager cliCommandManager;

    @Override
    public void run(final String... args) throws Exception {
        LOGGER.debug("Starting RabbitRPCCliRunner !!");
        final Command command = cliCommandManager.parse(args);
        doRpcMessageCall(command.getExchange(), command.getQueue(), command.getHeaders(), null);
    }

    private void doRpcMessageCall(final String exchange, final String routingKey, final Properties customHeaders, final byte[] body) {
        try {
            final MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationIdString(UUID.randomUUID().toString());
            messageProperties.setHeader("x-rest-verb", customHeaders.getProperty("x-rest-verb"));
            final Message response = rabbitTemplate.sendAndReceive(exchange, routingKey, new Message(body, messageProperties));
            LOGGER.info("{}", (response.getBody() == null || response.getBody().length == 0) ? "EMPTY_BODY" : new String(response.getBody()));
        } catch (AmqpException e) {
            LOGGER.error("Error while rpc call, ", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("Post Initialization checks for RabbitRpcCliRunner !!");
        Assert.notNull(rabbitTemplate, "RabbitTemplate cannot be null !!");
        LOGGER.debug("Post Initialization checks are done successfully for RabbitRpcCliRunner !!");
    }
}
