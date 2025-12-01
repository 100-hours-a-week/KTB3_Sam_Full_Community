package com.example.community.auth.config;

import com.example.community.auth.handler.CustomAccessDeniedHandler;
import com.example.community.auth.handler.CustomAuthenticationEntryPoint;
import com.example.community.auth.jwt.JwtCustomFilter;
import com.example.community.auth.cors.CorsCustomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                HttpMethod.POST, "/auth", "/users", "/images"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/users/email/**",
                                "/users/nickname/**",
                                "/images/**"
                        ).permitAll()

                        .anyRequest().authenticated())

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))


                .addFilterBefore(jwtCustomFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsCustomFilter, JwtCustomFilter.class);


        return http.build();
    }
}
