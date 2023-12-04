package com.botcamp.botcamp_api;

import com.botcamp.botcamp_api.config.BotcampAPIConfiguration;
import com.botcamp.common.utils.ApplicationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.net.UnknownHostException;

@SpringBootApplication
@Import(BotcampAPIConfiguration.class)
@Slf4j
public class BotcampAPIApplication {
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication application = new SpringApplication(BotcampAPIApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        ApplicationUtils.printAccessUrl(context, log);
    }

}
