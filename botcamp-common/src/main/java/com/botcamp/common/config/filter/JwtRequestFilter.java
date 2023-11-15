package com.botcamp.common.config.filter;

import com.botcamp.common.config.properties.SecurityConfigProperties;
import com.botcamp.common.entity.AEntity;
import com.botcamp.common.service.JwtUserDetailsService;
import com.botcamp.common.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter<ENTITY extends AEntity> extends OncePerRequestFilter {
    private final JwtUserDetailsService<ENTITY> gatewayUserDetailsService;
    private final SecurityConfigProperties securityConfigProperties;

    public JwtRequestFilter(SecurityConfigProperties securityConfigProperties,
                            @Lazy JwtUserDetailsService<ENTITY> service) {
        this.securityConfigProperties = securityConfigProperties;
        this.gatewayUserDetailsService = service;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!securityConfigProperties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = JwtUtils.getUsernameFromToken(jwtToken, securityConfigProperties.getJwt().getSecret());
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
            }
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.gatewayUserDetailsService.loadUserByUsername(username);
            logger.info("Request incoming from " + userDetails.getUsername());
            // if token is valid configure Spring Security to manually set
            // authentication
            if (JwtUtils.validateToken(jwtToken, userDetails, securityConfigProperties.getJwt().getSecret())) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
