package com.example.oauth2.config;


import com.example.oauth2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Properties;

@Configuration
@EnableMethodSecurity()
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final static String WHITE_LIST[]=new String[]{
            "/",
            "/auth/**",
            "/login/**"
    };


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests().requestMatchers(WHITE_LIST).permitAll()
                .requestMatchers(HttpMethod.POST,"/user/register").permitAll()
                .and()
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .formLogin((login) -> {
                    login.loginPage("/auth")
                            .defaultSuccessUrl("/cabinet/user/")
                            .failureForwardUrl("/auth/login");
                })
                .oauth2Login()
                .successHandler((request, response, authentication) -> {
                    DefaultOAuth2User principal =
                            (DefaultOAuth2User) authentication.getPrincipal();
                    userService.processOAuthPostLogin( principal);
                    response.sendRedirect("/user/cabinet");
                });

        return http.build();
    }




}
