package org.coderearth.rabbitrpccli;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by kunal_patel on 17/11/17.
 */
@SpringBootApplication(exclude = RabbitAutoConfiguration.class)
public class CliApplication {

    public static void main(String[] args) {
        final SpringApplication application = new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .registerShutdownHook(true)
                .logStartupInfo(false)
                .sources(CliConfiguration.class)
                .build();
        application.run(args).close();
    }

}
