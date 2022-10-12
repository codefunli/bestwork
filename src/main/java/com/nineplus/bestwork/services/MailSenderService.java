package com.nineplus.bestwork.services;

public interface MailSenderService {

	public void sendMailResetPassword(String toEmail, String username, String link);
	
}
