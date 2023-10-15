package com.botcamp.botcamp;

import com.botcamp.botcamp.config.BotCampConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(BotCampConfiguration.class)
public class BotCampApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotCampApplication.class, args);
    }

}
