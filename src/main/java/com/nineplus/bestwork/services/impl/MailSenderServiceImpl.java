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
	@Autowired
	private ThymleafService thymleafService;

	@Autowired
	private MessageUtils messageUtils;

	@Autowired
	private MailStorageService mailStorageService;

	public void sendMailResetPassword(String toEmail, String username, String link) {

		String email = messageUtils.getMessage(CommonConstants.SpringMail.M1U0006, null);
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
		String email = messageUtils.getMessage(CommonConstants.SpringMail.M1U0006, null);
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
		String host = messageUtils.getMessage(CommonConstants.SpringMail.M1H0004, null);
		String port = messageUtils.getMessage(CommonConstants.SpringMail.M1P0005, null);
		String email = messageUtils.getMessage(CommonConstants.SpringMail.M1U0006, null);
		String password = messageUtils.getMessage(CommonConstants.SpringMail.M1W0007, null);
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
		return session;
	}
}
