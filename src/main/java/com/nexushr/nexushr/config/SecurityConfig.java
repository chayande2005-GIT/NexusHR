package com.nexushr.nexushr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nexushr.nexushr.security.JwtFilter;
import com.nexushr.nexushr.security.JwtUtil;

@Configuration
public class SecurityConfig {

    /**
     * Create JwtFilter bean — JwtUtil is injected so the filter can validate tokens.
     * NOTE: JwtFilter is also annotated @Component, so remove that annotation from
     * JwtFilter.java to avoid Spring registering it twice (which causes double-filtering).
     */
    @Bean
    public JwtFilter jwtFilter(JwtUtil jwtUtil) {
        JwtFilter filter = new JwtFilter();
        filter.setJwtUtil(jwtUtil);  // ✅ was: new JwtFilter() — jwtUtil was never passed in
        return filter;
    }

    /**
     * Password encoder bean for BCrypt hashing
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security filter chain configuration
     * - /auth/** endpoints are public (no JWT required)
     * - POST /users allows new user registration
     * - All other endpoints require valid JWT token
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for stateless API
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (no JWT required)
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()  // Allow user registration

                // All other endpoints require authentication
                .anyRequest().authenticated()
            );

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter,
            org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}