package com.botcamp.gmail_gateway_api.controller;

import com.botcamp.common.response.GenericResponse;
import com.botcamp.common.utils.HttpUtils;
import com.botcamp.gmail_gateway_api.credentials.GmailCredential;
import com.botcamp.gmail_gateway_api.service.GmailCredentialsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.botcamp.common.config.SwaggerConfig.BEARER_AUTHENTICATION;
import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.*;

@RestController
@CrossOrigin
@RequestMapping(API_CREDENTIALS)
@Tag(name = GMAIL_CREDENTIALS_CONTROLLER)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
public class GmailCredentialsController {
    private static final String GMAIL_EMAIL_REQUEST_PARAM = "email";
    private final GmailCredentialsService gmailCredentialsService;

    public GmailCredentialsController(GmailCredentialsService gmailCredentialsService) {
        this.gmailCredentialsService = gmailCredentialsService;
    }

    @RequestMapping(value = GET_CREDENTIALS, method = RequestMethod.POST)
    @Operation(summary = "Gets all stored credentials")
    public ResponseEntity<GenericResponse> getCredentials() {
        try {
            Map<String, GmailCredential> credentials = gmailCredentialsService.getGmailCredentials();
            return HttpUtils.generateResponse(HttpStatus.OK, HttpUtils.SUCCESS, credentials);
        } catch (Exception e) {
            return HttpUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @RequestMapping(value = CREATE_CREDENTIALS, method = RequestMethod.POST)
    @Operation(summary = "Creates new credentials to request the Gmail Gateway")
    public ResponseEntity<GenericResponse> createNewCredentials(@RequestParam(value = GMAIL_EMAIL_REQUEST_PARAM) String gmailEmail) {
        try {
            GmailCredential gmailCredential = gmailCredentialsService.createGmailCredential(gmailEmail);
            return HttpUtils.generateResponse(HttpStatus.OK, HttpUtils.SUCCESS, gmailCredential);
        } catch (Exception e) {
            return HttpUtils.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
