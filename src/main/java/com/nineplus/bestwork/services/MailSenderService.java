package com.nineplus.bestwork.services;

import com.nineplus.bestwork.dto.CompanyUserReqDto;

public interface MailSenderService {

	public void sendMailResetPassword(String toEmail, String username, String link);
	public void sendMailRegisterUserCompany(String toEmail, CompanyUserReqDto companyUserReqDto, String linkLogin);
	
}
