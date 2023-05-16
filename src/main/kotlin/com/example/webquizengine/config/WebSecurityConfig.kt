package com.example.webquizengine.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val authenticationProvider: AuthenticationProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http.httpBasic()
            .and()
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/register", "/actuator/shutdown").permitAll()
            .anyRequest().authenticated()
            .and()
            .authenticationProvider(authenticationProvider)
            .build()

}