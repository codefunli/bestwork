package com.nineplus.bestwork.services;

/**
 * 
 * @author DiepTT
 *
 */
public interface MailSenderService {

	public void sendMailResetPassword(String toEmail, String username, String link);

	public void sendMailRegisterUserCompany();

}
