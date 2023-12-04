package com.botcamp.common.utils;

import org.slf4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ApplicationUtils {
    private static final String SPRING_APPLICATION_NAME = "spring.application.name";
    private static final String SERVER_PORT = "server.port";
    public static void printAccessUrl(ConfigurableApplicationContext context, Logger logger) throws UnknownHostException {
        Environment env = context.getEnvironment();
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String appName = env.getProperty(SPRING_APPLICATION_NAME);
        String serverPort = env.getProperty(SERVER_PORT);
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String[] profiles = env.getActiveProfiles();
        logger.info(
                "\n-----------------------------------------\n\t"
                        + "Application '{}' is running ! Access URLs:\n\t"
                        + "Local: \t\t{}://localhost:{}\t\t\t {}://localhost:{}/swagger-ui.html\n\t"
                        + "External: \t{}://{}:{}\t\t {}://{}:{}/swagger-ui.html\n\t"
                        + "Profiles(s): \t{}"
                        + "\n-----------------------------------------",
                appName,
                protocol,
                serverPort,
                protocol,
                serverPort,
                protocol,
                hostAddress,
                serverPort,
                protocol,
                hostAddress,
                serverPort,
                profiles);
    }
}
