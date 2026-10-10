package com.acromere.wx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain( HttpSecurity http ) {
		http.authorizeHttpRequests( authorizeRequests -> authorizeRequests
			// Allow public access to /api/**
			.requestMatchers( "/api/**", "/error" ).permitAll()
			// Require ADMIN role for /admin/**
			//.requestMatchers( "/admin/**" ).hasRole( "ADMIN" )
			// All other requests require authentication
			.anyRequest().authenticated() ).httpBasic( withDefaults() );

		return http.build();
	}

}
