package com.harvestiq.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(auth -> auth
                                .requestMatchers("/h2-console/**", "/css/**", "/images/**", "/error").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated());
                http.formLogin(form -> form.loginPage("/login")
                                .defaultSuccessUrl("/dashboard", true)
                                .permitAll());
                http.logout(logout -> logout
                                .logoutSuccessUrl("/login?logout")
                                .permitAll());
                http.headers(headers -> headers
                                .frameOptions(frame -> frame.sameOrigin()));
                http.csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()));
                return http.build();
        }

}
