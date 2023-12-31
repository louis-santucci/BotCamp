package com.botcamp.gmail_gateway_api.controller;

import com.botcamp.common.exception.EmailHandlingException;
import com.botcamp.common.exception.UnknownUserException;
import com.botcamp.common.mail.EmailResults;
import com.botcamp.common.response.EmailResponse;
import com.botcamp.common.response.GenericResponse;
import com.botcamp.common.utils.GzipUtils;
import com.botcamp.gmail_gateway_api.config.GatewayUser;
import com.botcamp.gmail_gateway_api.service.GmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.botcamp.common.config.SwaggerConfig.BEARER_AUTHENTICATION;
import static com.botcamp.common.endpoints.GmailGatewayEndpoint.*;
import static com.botcamp.common.utils.HttpUtils.SUCCESS;
import static com.botcamp.common.utils.HttpUtils.generateResponse;

@RestController
@RequestMapping(path = API_MAIL)
@SecurityRequirement(name = BEARER_AUTHENTICATION)
@Tag(name = MAIL_CONTROLLER)
public class MailController {

    private final GmailService gmailService;
    private final ObjectMapper objectMapper;

    public MailController(GmailService service,
                          ObjectMapper objectMapper) {
        this.gmailService = service;
        this.objectMapper = objectMapper;
    }


    @GetMapping(GET_LIST)
    public ResponseEntity<GenericResponse<Object>> getEmails(@RequestParam(name = BEGIN_DATE_QUERY_PARAM, required = false) String beginDate,
                                                     @RequestParam(name = END_DATE_QUERY_PARAM, required = false) String endDate,
                                                     @RequestParam(name = SENDER_QUERY_PARAM, required = false) String sender,
                                                     @RequestParam(name = SUBJECT_QUERY_PARAM, required = false) String subject,
                                                     @RequestParam(name = COMPRESS_QUERY_PARAM, defaultValue = "false") boolean compress,
                                                     Authentication authentication) {
        try {
            var userDetails = (GatewayUser) authentication.getPrincipal();
            EmailResults results = this.gmailService.getEmails(userDetails, beginDate, endDate, sender, subject);
            Object content = compress ? compressEmailResults(results) : new EmailResponse(results.getEmails(), results.getErrors());
            return generateResponse(HttpStatus.OK, SUCCESS, content);
        } catch (IOException | InterruptedException | NullPointerException | EmailHandlingException | UnknownUserException e) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    private byte[] compressEmailResults(EmailResults emailList) throws IOException {
        String emailResultsString = objectMapper.writeValueAsString(emailList);
        return GzipUtils.compress(emailResultsString);
    }
}
