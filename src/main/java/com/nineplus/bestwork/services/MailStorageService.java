package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.entity.MailStorageEntity;

/**
 * 
 * @author DiepTT
 *
 */
public interface MailStorageService {

	void saveMailRegisterUserCompToSendLater(String email, String companyName, String username, String password);

	List<MailStorageEntity> getTenFirstMails();

	void deleteMail(MailStorageEntity mailStorage);

}
