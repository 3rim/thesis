package com.erim.bachelor.config;

import com.erim.bachelor.data.Role;
import com.erim.bachelor.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Enable @PreAuthorize at Method-Level
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private final String ADMIN = Role.ADMIN.toString();
    private final String LIBRARIAN = Role.LIBRARIAN.toString();
    private final String LOAN_HELPER = Role.LOAN_HELPER.toString();
    private final String INVENTORY_HELPER = Role.INVENTORY_HELPER.toString();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                //Public API
                .requestMatchers("/api/v1/auth/**")
                    .permitAll()

                //Fix: ResponseStatusException always redirect to /error page which request authentication due to the latest spring version
                .requestMatchers("/error")
                    .permitAll()

                //Configure Borrower-Admin endpoints
                .requestMatchers(HttpMethod.POST, "/api/v1/borrowers/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/borrowers/**").hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.PUT, "/api/v1/borrowers/**").hasAuthority(ADMIN)
                //Configure Inventory endpoints
                .requestMatchers( "/api/v1/inventory/**").hasAnyAuthority(ADMIN,LIBRARIAN,INVENTORY_HELPER)
                .requestMatchers( "/api/v1/loan/**").hasAnyAuthority(ADMIN,LIBRARIAN,LOAN_HELPER)

                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
