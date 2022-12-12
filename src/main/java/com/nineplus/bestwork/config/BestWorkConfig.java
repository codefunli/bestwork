package com.nineplus.bestwork.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

@Configuration
@EnableScheduling
public class BestWorkConfig {
	private static final String[] BASE_NAMES = { "classpath:i18n/messages" };
	

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(BASE_NAMES);
		messageSource.setCacheSeconds(10); // reload messages every 10 seconds
		messageSource.setUseCodeAsDefaultMessage(false);
		messageSource.setDefaultEncoding("UTF-8");

		return messageSource;
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};

	@Bean
	public AcceptHeaderLocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
		localeResolver.setSupportedLocales(Arrays.asList(new Locale("en"), new Locale("vi")));
		localeResolver.setDefaultLocale(new Locale("en"));
		return localeResolver;
	}
	@Bean
	public LocaleResolver sessionLocaleResolver() {
		return new AcceptHeaderLocaleResolver();
	}
}
