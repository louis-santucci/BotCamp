package com.botcamp.botcamp_api;

import com.botcamp.botcamp_api.config.BotcampAPIConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BotcampAPIConfiguration.class)
public class BotcampAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotcampAPIApplication.class, args);
    }

}
