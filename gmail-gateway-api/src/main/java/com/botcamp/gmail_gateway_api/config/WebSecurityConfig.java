package com.botcamp.gmail_gateway_api.config;

import com.botcamp.gmail_gateway_api.config.filter.JwtAuthenticationEntryPoint;
import com.botcamp.gmail_gateway_api.config.filter.JwtRequestFilter;
import com.botcamp.gmail_gateway_api.config.properties.SecurityConfigProperties;
import com.botcamp.gmail_gateway_api.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.AUTH;
import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.V1_AUTH;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Import(SecurityConfigProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    private final SecurityConfigProperties securityConfigProperties;


    public WebSecurityConfig(JwtAuthenticationEntryPoint entryPoint,
                             JwtRequestFilter requestFilter,
                             SecurityConfigProperties securityConfigProperties) {
        this.jwtAuthenticationEntryPoint = entryPoint;
        this.jwtRequestFilter = requestFilter;
        this.securityConfigProperties = securityConfigProperties;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type","accept","Origin","Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth,
                                JwtUserDetailsService jwtUserDetailsService,
                                PasswordEncoder passwordEncoder) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder);
    }



    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        if (this.securityConfigProperties.isEnabled()) {
            configureEnabledSecurity(httpSecurity);
        } else {
            configureDisabledSecurity(httpSecurity);
        }
    }

    private void configureDisabledSecurity(HttpSecurity httpSecurity) throws Exception {
        // We don't need CSRF for this example
        httpSecurity.csrf().disable()
                .cors()
                .and()
                // dont authenticate this particular request
                .authorizeRequests().antMatchers("/**").permitAll();
    }

    private void configureEnabledSecurity(HttpSecurity httpSecurity) throws Exception {
        String authUrl = V1_AUTH + AUTH;
        // We don't need CSRF for this example
        httpSecurity.csrf().disable()
                .cors()
                .and()
                // dont authenticate this particular request
                .authorizeRequests().antMatchers(authUrl).permitAll().anyRequest().authenticated()
                // all other requests need to be authenticated
                .and()
                // make sure we use stateless session; session won't be used to
                // store user's state.
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
