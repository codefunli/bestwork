package com.nineplus.bestwork.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.entity.MailStorageEntity;
import com.nineplus.bestwork.repository.MailStorageRepository;
import com.nineplus.bestwork.services.MailStorageService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.EncryptionUtils;
import com.nineplus.bestwork.utils.MessageUtils;

/**
 * 
 * @author DiepTT
 *
 */

@Service
public class MailStorageServiceImpl implements MailStorageService {
	@Value("${url.origin}")
	private String url;

	@Autowired
	MailStorageRepository mailStorageRepository;

	@Autowired
	MessageUtils messageUtils;

	@Autowired
	EncryptionUtils encryptionUtils;

	@Override
	public void saveMailRegisterUserCompToSendLater(String toEmail, String companyName, String username,
			String password) {
		MailStorageEntity mailStorage = new MailStorageEntity();
		String subject = messageUtils.getMessage(CommonConstants.SpringMail.M1X0002, null);
		String linkLogin = url + "/login";
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("company", companyName);
		paramsMap.put("username", username);
		paramsMap.put("password", password);
		paramsMap.put("link", linkLogin);

		String paramMapToString = "";
		for (String key : paramsMap.keySet()) {
			paramMapToString += (key + "=" + paramsMap.get(key) + ", ");
		}

		try {
			mailStorage.setRecipient(toEmail);
			mailStorage.setSubject(subject);
			mailStorage.setParams(encryptionUtils.encrypt(paramMapToString, encryptionUtils.getSecret()));

			mailStorageRepository.save(mailStorage);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	@Override
	public List<MailStorageEntity> getTenFirstMails() {
		return this.mailStorageRepository.findTenFirstMails();
	}

	@Override
	public void deleteMail(MailStorageEntity mailStorage) {
		this.mailStorageRepository.delete(mailStorage);

	}
}
