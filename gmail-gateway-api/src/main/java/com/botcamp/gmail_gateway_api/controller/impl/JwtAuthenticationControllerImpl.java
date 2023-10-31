package com.botcamp.gmail_gateway_api.controller.impl;

import com.botcamp.gmail_gateway_api.config.properties.JwtConfigProperties;
import com.botcamp.gmail_gateway_api.controller.JwtAuthenticationController;
import com.botcamp.gmail_gateway_api.controller.request.JwtRequest;
import com.botcamp.gmail_gateway_api.controller.response.JwtResponse;
import com.botcamp.gmail_gateway_api.service.JwtUserDetailsService;
import com.botcamp.gmail_gateway_api.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.AUTH;
import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.V1_AUTH;

@CrossOrigin
@RestController
@RequestMapping(path = V1_AUTH)
public class JwtAuthenticationControllerImpl implements JwtAuthenticationController {

    private AuthenticationManager authManager;
    private JwtUserDetailsService jwtUserDetailsService;
    private JwtConfigProperties jwtProperties;

    public JwtAuthenticationControllerImpl(AuthenticationManager manager,
                                           JwtUserDetailsService jwtUserDetailsService,
                                           JwtConfigProperties jwtProperties) {
        this.authManager = manager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    @RequestMapping(value = AUTH, method = RequestMethod.POST)
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = JwtUtils.generateToken(userDetails, jwtProperties.getSecret(), jwtProperties.getTokenValidity());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
