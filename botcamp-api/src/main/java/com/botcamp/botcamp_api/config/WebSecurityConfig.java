package com.botcamp.botcamp_api.config;

import com.botcamp.botcamp_api.repository.entity.BotcampUserEntity;
import com.botcamp.botcamp_api.service.BotcampUserDetailsService;
import com.botcamp.common.config.CorsConfig;
import com.botcamp.common.config.SwaggerConfig;
import com.botcamp.common.config.filter.JwtAuthenticationEntryPoint;
import com.botcamp.common.config.filter.JwtRequestFilter;
import com.botcamp.common.config.properties.SecurityConfigProperties;
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

import static com.botcamp.botcamp_api.controller.ControllerEndpoint.API_AUTH;
import static com.botcamp.botcamp_api.controller.ControllerEndpoint.AUTH;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Import({
        SecurityConfigProperties.class,
        CorsConfig.class,
        JwtRequestFilter.class,
        JwtAuthenticationEntryPoint.class,
        SwaggerConfig.class
})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter<BotcampUserEntity> jwtRequestFilter;
    private final SecurityConfigProperties securityConfigProperties;

    private final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            API_AUTH + AUTH
    };

    public WebSecurityConfig(JwtAuthenticationEntryPoint entryPoint,
                             JwtRequestFilter<BotcampUserEntity> jwtRequestFilter,
                             SecurityConfigProperties securityConfigProperties) {
        this.jwtAuthenticationEntryPoint = entryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
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
                                BotcampUserDetailsService gatewayUserDetailsService,
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
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
