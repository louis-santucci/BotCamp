package com.botcamp.gmail_gateway_api.controller;

import com.botcamp.common.config.properties.SecurityConfigProperties;
import com.botcamp.gmail_gateway_api.controller.request.JwtRequest;
import com.botcamp.gmail_gateway_api.controller.response.JwtResponse;
import com.botcamp.gmail_gateway_api.service.GatewayUserDetailsService;
import com.botcamp.common.utils.HttpUtils;
import com.botcamp.common.utils.JwtUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import static com.botcamp.common.utils.HttpUtils.generateResponse;

@CrossOrigin
@RestController
@RequestMapping(path = V1_AUTH)
@ConditionalOnProperty(prefix = "security",
        name = "enabled",
        havingValue = "true")
public class JwtAuthenticationController {

    private final AuthenticationManager authManager;
    private final GatewayUserDetailsService gatewayUserDetailsService;
    private final SecurityConfigProperties securityConfigProperties;

    public JwtAuthenticationController(AuthenticationManager manager,
                                       GatewayUserDetailsService gatewayUserDetailsService,
                                       SecurityConfigProperties securityConfigProperties) {
        this.authManager = manager;
        this.gatewayUserDetailsService = gatewayUserDetailsService;
        this.securityConfigProperties = securityConfigProperties;
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

        final UserDetails userDetails = gatewayUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = JwtUtils.generateToken(userDetails, securityConfigProperties.getJwt().getSecret(), securityConfigProperties.getJwt().getTokenValidity());

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
