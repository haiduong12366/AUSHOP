package AUSHOP.Config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import AUSHOP.services.CustomUserDetailsService;
import AUSHOP.services.UserServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		final List<GlobalAuthenticationConfigurerAdapter> configurers = new ArrayList<>();
		configurers.add(new GlobalAuthenticationConfigurerAdapter() {
			@Override
			public void configure(AuthenticationManagerBuilder auth) throws Exception {
				// auth.doSomething()
			}
		});
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		return http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/").hasAnyAuthority("USER","ADMIN")
				.antMatchers("/new").hasAnyAuthority("ADMIN", "CREATOR")
				.antMatchers("/edit/**").hasAnyAuthority("ADMIN","EDITOR")
				.antMatchers("/delete/**").hasAnyAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/api/**").permitAll()
				.antMatchers("/api/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.httpBasic()
				.and()
				.formLogin().loginPage("/login").permitAll()
				.and()
				.logout().permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/403")
				.and()
				.build();
	}
}
