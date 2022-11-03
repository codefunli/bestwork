package com.nineplus.bestwork.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.nineplus.bestwork.entity.MailStorage;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.ThymleafService;
import com.nineplus.bestwork.utils.EncryptionUtils;

@Service
public class ThymleafServiceImpl implements ThymleafService {

	private static final String MAIL_TEMPLATE_BASE_NAME = "mail/MailMessages";
	private static final String MAIL_TEMPLATE_PREFIX = "/templates/";
	private static final String MAIL_TEMPLATE_SUFFIX = ".html";
	private static final String UTF_8 = "UTF-8";

	private static final String RESET_PASSWORD_TEMPLATE_NAME = "reset-password-mail-template";
	private static final String REGISTER_USER_COMPANY_TEMPLATE_NAME = "register-user-company-mail-template";

	private static TemplateEngine templateEngine;
	static {
		templateEngine = emailTemplateEngine();
	}

	@Autowired
	private EncryptionUtils encryptionUtils;

	private static TemplateEngine emailTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(htmlTemplateResolver());
		templateEngine.setTemplateEngineMessageSource(emailMessageSource());

		return templateEngine;
	}

	private static ResourceBundleMessageSource emailMessageSource() {
		final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(MAIL_TEMPLATE_BASE_NAME);
		return messageSource;
	}

	private static ITemplateResolver htmlTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
		templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding(UTF_8);
		templateResolver.setCacheable(false);

		return templateResolver;
	}

	public String getContentMailResetPassword(String username, String link) {
		final Context context = new Context();
		context.setVariable("username", username);
		context.setVariable("link", link);
		return templateEngine.process(RESET_PASSWORD_TEMPLATE_NAME, context);
	}

	@Override
	public String getContentMailRegisterUserCompany(MailStorage mailStorage) throws BestWorkBussinessException {
		final Context context = new Context();
		String paramString = encryptionUtils.decrypt(mailStorage.getParams(), encryptionUtils.getSecret());
		String[] keyValuePairs = paramString.split(", ");

		for (String pair : keyValuePairs) {
			String[] entry = pair.split("=");
			context.setVariable(entry[0].trim(), entry[1].trim());
		}
		return templateEngine.process(REGISTER_USER_COMPANY_TEMPLATE_NAME, context);
	}
}
