package com.devpro.shop16.conf;

import com.devpro.shop16.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecureConf extends WebSecurityConfigurerAdapter {
	
	@Autowired 
	private UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				// bat dau cau hinh: tat ca cac requests tu trinh duyet deu duoc bat trong ham nay

		//cho phep cac request static resources khong bi rang buoc(permitAll)
		.antMatchers("/css/**", "/js/**", "/upload/**", "/login", "/logout").permitAll()

		//cac request kieu: "/admin/" la phai dang nhap (authenticated)
		.antMatchers("/admin/**").hasAuthority("ADMIN")

		.and()
		
		//cau hinh trang dang nhap
		//login la 1 action
		.formLogin().loginPage("/login").loginProcessingUrl("/perform_login")
		.successHandler(authenticationSuccessHandler())
//		.defaultSuccessUrl("/admin", true)
		.failureUrl("/login?login_error=true")

		.and()

		//cau hinh cho phan logout
		.logout().logoutUrl("/logout").logoutSuccessUrl("/home").invalidateHttpSession(true)
		.deleteCookies("JSESSIONID");
//		.and().rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400)
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder(4));
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler(){
		return new UrlAuthenticationSuccessHandler();
	}


}
