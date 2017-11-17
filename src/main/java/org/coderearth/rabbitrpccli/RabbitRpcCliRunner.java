package org.coderearth.rabbitrpccli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.UUID;

@Component
public class RabbitRpcCliRunner implements ApplicationRunner, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitRpcCliRunner.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        LOGGER.debug("Starting RabbitRPCCliRunner !!");
        final String exchange;
        if (args.containsOption("exchange")) {
            exchange = args.getOptionValues("exchange").get(0);
        } else {
            LOGGER.error("CommandLineOption (exchange) is not provided !!");
            throw new RuntimeException("CommandLineOption (exchange) is not provided !!");
        }

        final String routingKey;
        if (args.containsOption("queue") || args.containsOption("key")) {
            routingKey = args.containsOption("queue")
                    ? args.getOptionValues("queue").get(0)
                    : args.getOptionValues("key").get(0);
        } else {
            LOGGER.error("CommandLineOption (queue/key) is not provided !!");
            throw new RuntimeException("CommandLineOption (queue/key) is not provided !!");
        }
        doRpcMessageCall(exchange, routingKey);
    }

    private void doRpcMessageCall(final String exchange, final String routingKey) {
        final byte[] body = null;
        try {
            final MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationIdString(UUID.randomUUID().toString());
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
