package com.botcamp.common.config.filter;

import com.botcamp.common.service.TokenExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenExtractionFilter extends OncePerRequestFilter {

    private TokenExtractor tokenExtractor;

    private static final String AUTH_HEADER = "Authorization";

    public JwtTokenExtractionFilter(TokenExtractor tokenExtractor) {
        this.tokenExtractor = tokenExtractor;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(AUTH_HEADER);
        this.tokenExtractor.setJwtToken(token);

        filterChain.doFilter(request, response);
    }
}
