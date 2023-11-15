package com.botcamp.gmail_gateway_api.controller;

import com.botcamp.gmail_gateway_api.mailing.Email;
import com.botcamp.gmail_gateway_api.mailing.EmailHandlingException;
import com.botcamp.gmail_gateway_api.service.GmailService;
import com.botcamp.common.utils.GzipUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.GET_LIST;
import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.V1_MAIL;
import static com.botcamp.common.utils.HttpUtils.SUCCESS;
import static com.botcamp.common.utils.HttpUtils.generateResponse;

@RestController
@RequestMapping(path = V1_MAIL)
public class MailController {

    private static final String BEGIN_DATE_QUERY_PARAM = "beginDate";
    private static final String END_DATE_QUERY_PARAM = "endDate";
    private static final String TOPIC_QUERY_PARAM = "sender";
    private static final String SUBJECT_QUERY_PARAM = "subject";
    private static final String COMPRESS_QUERY_PARAM = "compress";

    private final GmailService gmailService;
    private final ObjectMapper objectMapper;

    public MailController(GmailService service,
                          ObjectMapper objectMapper) {
        this.gmailService = service;
        this.objectMapper = objectMapper;
    }


    @GetMapping(GET_LIST)
    public ResponseEntity<?> getEmails(@RequestParam(name = BEGIN_DATE_QUERY_PARAM, required = false) String beginDate,
                                       @RequestParam(name = END_DATE_QUERY_PARAM, required = false) String endDate,
                                       @RequestParam(name = TOPIC_QUERY_PARAM, required = false) String sender,
                                       @RequestParam(name = SUBJECT_QUERY_PARAM, required = false) String subject,
                                       @RequestParam(name = COMPRESS_QUERY_PARAM, defaultValue = "false") boolean compress) {
        try {
            List<Email> results = this.gmailService.getEmails(beginDate, endDate, sender, subject);
            Object content = compress ? compressEmailResults(results) : results;
            return generateResponse(HttpStatus.OK, true, SUCCESS, content);
        } catch (IOException | InterruptedException | NullPointerException | EmailHandlingException e) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }
    }

    private String compressEmailResults(List<Email> emailList) throws JsonProcessingException {
        InputStream is = new ByteArrayInputStream(objectMapper.writeValueAsBytes(emailList));
        ByteArrayOutputStream baos = GzipUtils.compress(is);

        return baos.toString(StandardCharsets.UTF_8);
    }
}
