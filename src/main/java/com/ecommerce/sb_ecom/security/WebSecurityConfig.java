package com.ecommerce.sb_ecom.security;

import com.ecommerce.sb_ecom.security.jwt.AuthEntryPointJwt;
import com.ecommerce.sb_ecom.security.jwt.AuthTokenFilter;
import com.ecommerce.sb_ecom.security.services.UserDetailsServiceImpl;
import com.ecommerce.sb_ecom.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    // JWT Filter
//    @Bean
//    public AuthTokenFilter authenticationJwtTokenFilter() {
//        return new AuthTokenFilter(jwtUtils, userDetailsServiceImpl);
//    }

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Correct DaoAuthenticationProvider (works in your version)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.authenticationProvider(authenticationProvider());

        return builder.build();
    }

    // SECURITY FILTER CHAIN
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().authenticated()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
               // );

        // Add JWT Filter
        //http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}