package com.example.community.auth.config;

import com.example.community.auth.JwtCustomFilter;
import com.example.community.auth.cors.CorsCustomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtCustomFilter jwtCustomFilter;
    private final CorsCustomFilter corsCustomFilter;

    SecurityConfig(JwtCustomFilter jwtCustomFilter, CorsCustomFilter corsCustomFilter) {
        this.jwtCustomFilter = jwtCustomFilter;
        this.corsCustomFilter = corsCustomFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .addFilterBefore(jwtCustomFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsCustomFilter, JwtCustomFilter.class);


        return http.build();
    }
}
