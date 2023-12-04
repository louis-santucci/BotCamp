package com.botcamp.gmail_gateway_api;

import com.botcamp.common.utils.ApplicationUtils;
import com.botcamp.gmail_gateway_api.config.GmailGatewayConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import java.net.UnknownHostException;

@SpringBootApplication
@Import(GmailGatewayConfiguration.class)
@Slf4j
public class GmailGatewayApiApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication application = new SpringApplication(GmailGatewayApiApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        ApplicationUtils.printAccessUrl(context, log);
    }

}
