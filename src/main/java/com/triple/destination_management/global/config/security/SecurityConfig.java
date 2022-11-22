package com.triple.destination_management.global.config.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.triple.destination_management.global.config.security.jwt.JwtProvider;
import com.triple.destination_management.global.config.security.jwt.filter.JwtAuthenticationFilter;
import com.triple.destination_management.global.config.security.jwt.filter.JwtAuthorizationFilter;
import com.triple.destination_management.global.config.security.jwt.handler.JwtAuthenticationDeniedHandler;
import com.triple.destination_management.global.config.security.jwt.handler.JwtAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] USER_ALLOWED_URI_PATTERN = new String[] {
		"/api/v1/trip/**"
	};

	private final JwtProvider jwtProvider;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable()
			.formLogin()
			.disable()
			.httpBasic()
			.disable();

		http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), jwtProvider),
				UsernamePasswordAuthenticationFilter.class
			)
			.addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), jwtProvider),
				BasicAuthenticationFilter.class
			);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests()
			// .antMatchers(USER_ALLOWED_URI_PATTERN).hasRole("USER")
			.anyRequest().permitAll();

		http.exceptionHandling()
			.accessDeniedHandler(new JwtAuthenticationDeniedHandler())
			.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
}
