package com.botcamp.gmail_gateway_api.config.filter;

import com.botcamp.gmail_gateway_api.config.BotcampUser;
import com.botcamp.gmail_gateway_api.config.properties.JwtConfigProperties;
import com.botcamp.gmail_gateway_api.service.JwtUserDetailsService;
import com.botcamp.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Setter;
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
@Setter
public class JwtRequestFilter extends OncePerRequestFilter {
    private JwtUserDetailsService jwtUserDetailsService;
    private JwtConfigProperties jwtProperties;

    public JwtRequestFilter(JwtConfigProperties jwtConfigProperties) {
        this.jwtProperties = jwtConfigProperties;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = JwtUtils.getUsernameFromToken(jwtToken, jwtProperties.getSecret());
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired");
            }
        }

        // Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            BotcampUser userDetails = (BotcampUser) this.jwtUserDetailsService.loadUserByUsername(username);
            logger.info("Request incoming from " + userDetails.getGmailEmail());
            // if token is valid configure Spring Security to manually set
            // authentication
            if (JwtUtils.validateToken(jwtToken, userDetails, jwtProperties.getSecret())) {

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
