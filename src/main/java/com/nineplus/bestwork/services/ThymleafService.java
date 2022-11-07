package com.nineplus.bestwork.services;

import com.nineplus.bestwork.entity.MailStorageEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface ThymleafService {
	public String getContentMailResetPassword(String username, String link);

	public String getContentMailRegisterUserCompany(MailStorageEntity mailStorage) throws BestWorkBussinessException;
}
