package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.CompanyUserReqDto;
import com.nineplus.bestwork.entity.MailStorage;

/**
 * 
 * @author DiepTT
 *
 */
public interface MailStorageService {

	void saveMailRegisterUserCompToSendLater(String email, String companyName, String username, String password);

	List<MailStorage> get10FirstMails();

	void deleteMail(MailStorage mailStorage);

}
