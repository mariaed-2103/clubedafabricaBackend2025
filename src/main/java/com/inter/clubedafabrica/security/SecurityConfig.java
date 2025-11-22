package com.inter.clubedafabrica.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desativa proteção CSRF
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Libera TODOS os endpoints
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(login -> login.disable()); // Desativa tela de login

        return http.build();
    }
}
