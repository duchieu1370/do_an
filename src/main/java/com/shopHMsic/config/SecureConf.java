package com.shopHMsic.config;

import com.shopHMsic.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecureConf {
	
	@Autowired 
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/css/**", "/js/**", "/upload/**", "/login", "/logout").permitAll()
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers("/api/admin/products/**", "/api/admin/categories/**").hasAnyAuthority("ADMIN", "STAFF")
				.requestMatchers(org.springframework.http.HttpMethod.GET, "/api/admin/users/**", "/api/admin/roles/**", "/api/admin/orders/**", "/api/admin/contacts/**", "/api/admin/subscribes/**").hasAnyAuthority("ADMIN", "STAFF")
				.requestMatchers("/api/admin/**").hasAuthority("ADMIN")
				.requestMatchers("/admin/product/**").hasAnyAuthority("ADMIN", "STAFF")
				.requestMatchers("/admin", "/admin/home").hasAnyAuthority("ADMIN", "STAFF")
				.requestMatchers("/admin/**").hasAuthority("ADMIN")
				.anyRequest().permitAll()
			)
			.addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(401);
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write("{\"code\":401,\"message\":\"Vui lòng đăng nhập để thực hiện thao tác này!\"}");
				})
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					response.setStatus(403);
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write("{\"code\":403,\"message\":\"Bạn không có quyền truy cập tài nguyên này!\"}");
				})
			);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://127.0.0.1:5173"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
