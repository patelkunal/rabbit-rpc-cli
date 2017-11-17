package org.coderearth.rabbitrpccli;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/spring/rabbitmq-context.xml")
@ComponentScan("org.coderearth.rabbitrpccli")
public class CliConfiguration {

}
