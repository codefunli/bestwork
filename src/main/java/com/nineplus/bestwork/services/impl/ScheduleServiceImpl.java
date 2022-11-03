package com.nineplus.bestwork.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.services.MailSenderService;
import com.nineplus.bestwork.services.ScheduleService;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	public static boolean isCompleted;

	@Autowired
	private MailSenderService mailSenderService;

	@Scheduled(fixedDelay = 3000)
	public void sendMailRegisterUserCompany() {
		if (isCompleted) {
			mailSenderService.sendMailRegisterUserCompany();
		}
	}
}