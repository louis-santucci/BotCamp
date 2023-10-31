package com.botcamp.gmail_gateway_api.controller;

import com.botcamp.gmail_gateway_api.controller.request.JwtRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface JwtAuthenticationController {
    ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authenticationRequest) throws Exception;
}
