package com.coded.spring.ordering.authentication

import org.springframework.context.annotation.*
import org.springframework.security.config.annotation.web.builders.*
import org.springframework.security.config.annotation.web.configuration.*
import org.springframework.security.crypto.bcrypt.*
import org.springframework.security.crypto.password.*
import org.springframework.security.web.*


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userDetailsService: CustomUserDetailsService
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() } // For testing only

            .authorizeHttpRequests {
                it.requestMatchers("/menus/**").permitAll() // public route
                it.requestMatchers("/register").permitAll()
                it.requestMatchers("/orders/**").authenticated() // protected route

                    .anyRequest().authenticated()
            }
            .formLogin { it.defaultSuccessUrl("/menus", true) }
            .userDetailsService(userDetailsService)

        return http.build()
    }
}
