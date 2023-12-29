package com.botcamp.common.gateway_credentials;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.util.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

@Slf4j(topic = "[BROWSER][OAUTH2]")
public class OAuthBrowser implements AuthorizationCodeInstalledApp.Browser {

    @Override
    public void browse(String url) {
        Preconditions.checkNotNull(url);
        log.info("Please open the following address in your browser: {}", url);

        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    log.info("Attempting to open that address in the default browser now...");
                    desktop.browse(URI.create(url));
                }
            }
        } catch (IOException var2) {
            log.info("Unable to open browser", var2);
        } catch (InternalError var3) {
            log.warn("Unable to open browser", var3);
        }
    }
}
