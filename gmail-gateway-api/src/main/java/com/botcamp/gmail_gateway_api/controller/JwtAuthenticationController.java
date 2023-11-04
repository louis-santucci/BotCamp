package com.botcamp.gmail_gateway_api.controller;

import com.botcamp.gmail_gateway_api.config.properties.JwtConfigProperties;
import com.botcamp.gmail_gateway_api.controller.request.JwtRequest;
import com.botcamp.gmail_gateway_api.controller.response.JwtResponse;
import com.botcamp.gmail_gateway_api.service.JwtUserDetailsService;
import com.botcamp.utils.HttpUtils;
import com.botcamp.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.AUTH;
import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.V1_AUTH;
import static com.botcamp.utils.HttpUtils.generateResponse;

@CrossOrigin
@RestController
@RequestMapping(path = V1_AUTH)
public class JwtAuthenticationController {

    private final AuthenticationManager authManager;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtConfigProperties jwtProperties;

    public JwtAuthenticationController(AuthenticationManager manager,
                                       JwtUserDetailsService jwtUserDetailsService,
                                       JwtConfigProperties jwtProperties) {
        this.authManager = manager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtProperties = jwtProperties;
    }

    @RequestMapping(value = AUTH, method = RequestMethod.POST)
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (BadCredentialsException | DisabledException e) {
            return generateResponse(HttpStatus.UNAUTHORIZED, false, e.getMessage(), null);
        } catch (Exception e) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, e.getMessage(), null);
        }

        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = JwtUtils.generateToken(userDetails, jwtProperties.getSecret(), jwtProperties.getTokenValidity());

        return generateResponse(HttpStatus.OK, true, HttpUtils.SUCCESS, new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }
}
