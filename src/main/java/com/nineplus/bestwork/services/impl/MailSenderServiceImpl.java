package com.nineplus.bestwork.services.impl;

import java.util.List;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.entity.MailStorageEntity;
import com.nineplus.bestwork.services.MailSenderService;
import com.nineplus.bestwork.services.MailStorageService;
import com.nineplus.bestwork.services.ThymleafService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.MessageUtils;

/**
 * 
 * @author DiepTT
 *
 */
@Service
public class MailSenderServiceImpl implements MailSenderService {
	private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";
	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private String port;

	@Value("${spring.mail.username}")
	private String email;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private boolean propAuth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean propTlsEnale;

	@Autowired
	private ThymleafService thymleafService;

	@Autowired
	private MessageUtils messageUtils;

	@Autowired
	private MailStorageService mailStorageService;

	public void sendMailResetPassword(String toEmail, String username, String link) {

		String subject = messageUtils.getMessage(CommonConstants.SpringMail.M1X0003, null);
		Message message = new MimeMessage(mailCommon());
		try {
			message.setRecipients(Message.RecipientType.TO, new InternetAddress[] { new InternetAddress(toEmail) });
			message.setFrom(new InternetAddress(email));
			message.setSubject(subject);
			message.setContent(thymleafService.getContentMailResetPassword(username, link), CONTENT_TYPE_TEXT_HTML);
			Transport.send(message);
			System.out.println("Send mail successfully!");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void sendMailRegisterUserCompany() {
		Message message = new MimeMessage(mailCommon());

		List<MailStorageEntity> mailList = mailStorageService.getTenFirstMails();
		if (mailList.isEmpty()) {
			System.out.println("Not found any mail!");
			ScheduleServiceImpl.isCompleted = false;
		} else {
			ScheduleServiceImpl.isCompleted = false;
			for (MailStorageEntity mailStorage : mailList) {
				try {
					message.setRecipients(Message.RecipientType.TO,
							new InternetAddress[] { new InternetAddress(mailStorage.getRecipient()) });
					message.setFrom(new InternetAddress(email));
					message.setSubject(mailStorage.getSubject());
					message.setContent(thymleafService.getContentMailRegisterUserCompany(mailStorage),
							CONTENT_TYPE_TEXT_HTML);
					Transport.send(message);

					mailStorageService.deleteMail(mailStorage);

					System.out.println("Send mail to " + mailStorage.getRecipient() + " successfully!");

				} catch (Exception e) {
					System.out.println("Failed to send mail to " + mailStorage.getRecipient() + "!");
				}
			}
			ScheduleServiceImpl.isCompleted = true;
		}
	}

	private Session mailCommon() {

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.starttls.enable", propTlsEnale);
		props.put("mail.smtp.auth", propAuth);
		props.put("mail.smtp.port", port);

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});
		return session;
	}
}
