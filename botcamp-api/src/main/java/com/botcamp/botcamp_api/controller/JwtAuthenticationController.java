package com.botcamp.botcamp_api.controller;

import com.botcamp.botcamp_api.service.BotcampUserDetailsService;
import com.botcamp.common.config.properties.SecurityConfigProperties;
import com.botcamp.common.jwt.JwtToken;
import com.botcamp.common.request.JwtRequest;
import com.botcamp.common.response.GenericResponse;
import com.botcamp.common.response.JwtResponse;
import com.botcamp.common.utils.HttpUtils;
import com.botcamp.common.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.botcamp.common.endpoints.BotcampApiEndpoint.*;
import static com.botcamp.common.utils.HttpUtils.generateResponse;

@CrossOrigin
@RestController
@RequestMapping(path = API_AUTH)
@ConditionalOnProperty(prefix = "security",
        name = "enabled",
        havingValue = "true")
@Tag(name = JWT_AUTH_CONTROLLER)
public class JwtAuthenticationController {

    private final AuthenticationManager authManager;
    private final BotcampUserDetailsService gatewayUserDetailsService;
    private final SecurityConfigProperties securityConfigProperties;

    public JwtAuthenticationController(AuthenticationManager manager,
                                       BotcampUserDetailsService gatewayUserDetailsService,
                                       SecurityConfigProperties securityConfigProperties) {
        this.authManager = manager;
        this.gatewayUserDetailsService = gatewayUserDetailsService;
        this.securityConfigProperties = securityConfigProperties;
    }

    @PostMapping(value = AUTH)
    @Operation(summary = "Authenticates the user using username/password")
    public ResponseEntity<GenericResponse<JwtResponse>> createAuthToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (BadCredentialsException | DisabledException e) {
            return generateResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            return generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }

        final UserDetails userDetails = gatewayUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final JwtToken token = JwtUtils.generateToken(userDetails,
                securityConfigProperties.getJwt().getSecret(),
                securityConfigProperties.getJwt().getTokenValidity(),
                securityConfigProperties.getJwt().getUnit());

        return generateResponse(HttpStatus.OK, HttpUtils.SUCCESS, new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws AuthenticationException {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }
}
