package com.nineplus.bestwork.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nineplus.bestwork.filter.CustomAuthenticationFilter;
import com.nineplus.bestwork.filter.CustomAuthorizationFilter;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SercurityConfiguration implements EnvironmentAware {

	public static String PUBLIC_URL[] = {"/api/v1/auth/**"};
	public static String IGNORE_URL[] = {};
	@Value("${allow.origins}")
	private String allowOrigins;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	private Environment environment;

	@Override
	public void setEnvironment(final Environment environment) {
		this.environment = environment;
	}

	/**
	 * Configure.
	 *
	 * @param http the http
	 * @throws Exception the exception
	 */
	@Bean
	@Order(1)
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().configurationSource(corsConfigurationSource()).and().csrf().disable();

		// No session will be created or used by spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Entry points
		// uncheck authorizeRequest
		http.authorizeRequests().antMatchers(PUBLIC_URL).permitAll();
		http.authorizeRequests().anyRequest().authenticated();
		http.apply(customDsl());
		http.logout().logoutSuccessHandler(new LogoutSuccessHandler() {

			//Delete cookie when user logout
			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				response.setStatus(HttpStatus.OK.value());
				;
			}
		}).logoutRequestMatcher(new AntPathRequestMatcher("/logout")).clearAuthentication(true)
				.deleteCookies(CommonConstants.Authentication.ACCESS_COOKIE)
				.deleteCookies(CommonConstants.Authentication.REFRESH_COOKIE);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();
		String[] allowOriginsArr = allowOrigins.split(",");
		List<String> allowOrigins = Arrays.asList(allowOriginsArr);
		configuration.setAllowedOriginPatterns(allowOrigins);
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers(IGNORE_URL);
	}

	@Bean
	public CustomAuthorizationFilter authorizationFilter() throws Exception {
		return new CustomAuthorizationFilter(environment.getProperty("app.login.jwtPrefix"),
				environment.getProperty("app.login.jwtSecretKey"), environment.getProperty("app.login.jwtExpiration"),
				PUBLIC_URL);
	}

	public MyCustomDsl customDsl() {
		return new MyCustomDsl();
	}

	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			http.addFilter(new CustomAuthenticationFilter(authenticationManager,
					environment.getProperty("app.login.jwtSecretKey"), environment.getProperty("app.login.jwtPrefix"),
					environment.getProperty("app.login.jwtExpiration")));
			http.addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		}
	}

}
