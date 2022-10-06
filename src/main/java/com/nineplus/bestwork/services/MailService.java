package com.nineplus.bestwork.services;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.MessageUtils;

@Service
public class MailService {
	private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";
	@Autowired
	private ThymleafService thymleafService;

	@Autowired
	private MessageUtils messageUtils;

	public void sendMail(String toEmail, String username, String link) {
		String host = messageUtils.getMessage(CommonConstants.SpringMail.M1H0004, null);
		String port = messageUtils.getMessage(CommonConstants.SpringMail.M1P0005, null);
		String email = messageUtils.getMessage(CommonConstants.SpringMail.M1U0006, null);
		String password = messageUtils.getMessage(CommonConstants.SpringMail.M1W0007, null);
		String subject = messageUtils.getMessage(CommonConstants.SpringMail.M1X0003, null);

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", port);

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});
		Message message = new MimeMessage(session);
		try {
			message.setRecipients(Message.RecipientType.TO,
					new InternetAddress[] {new InternetAddress(toEmail)});
			message.setFrom(new InternetAddress(email));
			message.setSubject(subject);
			message.setContent(thymleafService.getContent(username, link), CONTENT_TYPE_TEXT_HTML);
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
