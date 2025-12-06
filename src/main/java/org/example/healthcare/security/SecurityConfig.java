package org.example.healthcare.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/translate/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN", "NGO")
                        .requestMatchers("/api/health-alerts/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN", "NGO", "DONOR")
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/treatments/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN", "DONOR")
                        .requestMatchers("/api/donations/**").hasAnyRole("ADMIN", "DONOR", "NGO")
                        .requestMatchers("/api/equipment/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN", "NGO", "DONOR")
                        .requestMatchers("/api/supplies/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN", "NGO", "DONOR")
                        .requestMatchers("/api/inventory/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN", "NGO", "DONOR")
                        .requestMatchers("/api/ngo/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN", "NGO", "DONOR")
                        .requestMatchers("/api/medical-records/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        .requestMatchers("/api/patients/**").hasAnyRole("PATIENT", "DOCTOR", "ADMIN")
                        .requestMatchers("/api/donors/**").hasAnyRole("DONOR", "ADMIN")
                        .requestMatchers("/api/consultations/**").hasAnyRole("DOCTOR", "ADMIN", "PATIENT")
                        .requestMatchers("/api/doctor/**").hasAnyRole("DOCTOR", "ADMIN", "PATIENT")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
