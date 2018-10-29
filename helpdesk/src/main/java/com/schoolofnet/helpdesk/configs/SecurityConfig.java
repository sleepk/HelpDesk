package com.schoolofnet.helpdesk.configs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/login")
		.permitAll()
		.antMatchers("/registration")
		.permitAll()
				.antMatchers("/**")
				.hasAnyAuthority("admin", "user")
				.anyRequest()
				.authenticated().and().httpBasic()
				.authenticationEntryPoint(authEntryPoint);
	}

	@Override
	public void configure(WebSecurity webSecurity) {
		webSecurity.ignoring().antMatchers("/static/**", "/js/**", "/css/**", "/videos/**", "/images/**",
				"/resources/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().usersByUsernameQuery(
				" select usr.email, usr.password, usr.active from users usr where usr.email = ? and usr.active = 1")
				.authoritiesByUsernameQuery(" select usr.email, rl.name from users usr "
						+ " inner join users_roles usrr on (usr.id = usrr.user_id) "
						+ " inner join roles rl on (usrr.role_id = rl.id)" + " where usr.email = ? "
						+ " and   usr.active = 1")
				.dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
	}
}