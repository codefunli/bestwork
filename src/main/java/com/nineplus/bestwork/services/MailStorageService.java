package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.CompanyUserReqDto;
import com.nineplus.bestwork.entity.MailStorage;

public interface MailStorageService {

	void saveMailRegisterUserCompToSendLater(String email, CompanyUserReqDto companyReqDto);

	List<MailStorage> get10FirstMails();

	void deleteMail(MailStorage mailStorage);

}
