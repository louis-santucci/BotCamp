package com.botcamp.gmail_gateway_api;

import com.botcamp.gmail_gateway_api.config.GmailGatewayConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(GmailGatewayConfiguration.class)
public class GmailGatewayApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmailGatewayApiApplication.class, args);
    }

}
