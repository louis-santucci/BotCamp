package com.botcamp.gmail_gateway_api.config;

import com.botcamp.common.config.CorsConfig;
import com.botcamp.common.config.properties.SecurityConfigProperties;
import com.botcamp.common.config.filter.JwtRequestFilter;
import com.botcamp.common.config.filter.JwtAuthenticationEntryPoint;
import com.botcamp.gmail_gateway_api.repository.entity.GatewayUserEntity;
import com.botcamp.gmail_gateway_api.service.GatewayUserDetailsService;
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

import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.AUTH;
import static com.botcamp.gmail_gateway_api.controller.ControllerEndpoint.V1_AUTH;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Import({
        SecurityConfigProperties.class,
        JwtRequestFilter.class,
        JwtAuthenticationEntryPoint.class,
        CorsConfig.class
})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtRequestFilter<GatewayUserEntity> jwtRequestFilter;
    private final SecurityConfigProperties securityConfigProperties;


    public WebSecurityConfig(JwtAuthenticationEntryPoint entryPoint,
                             JwtRequestFilter<GatewayUserEntity> requestFilter,
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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth,
                                GatewayUserDetailsService gatewayUserDetailsService,
                                PasswordEncoder passwordEncoder) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(gatewayUserDetailsService).passwordEncoder(passwordEncoder);
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
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/**").permitAll();
    }

    private void configureEnabledSecurity(HttpSecurity httpSecurity) throws Exception {
        String authUrl = V1_AUTH + AUTH;
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(authUrl).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
