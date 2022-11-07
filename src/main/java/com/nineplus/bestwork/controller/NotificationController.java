package com.nineplus.bestwork.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.NotificationResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.NotificationService;
import com.nineplus.bestwork.utils.CommonConstants;

@RestController
@RequestMapping(value = "api/v1/notifications")
public class NotificationController extends BaseController {

	@Autowired
	private NotificationService notificationService;

	@GetMapping("/list")
	public ResponseEntity<? extends Object> getAllNotificationsByUser() {

		List<NotificationResDto> notificationList = null;

		try {
			notificationList = notificationService.getAllNotificationsByUser();
		} catch (BestWorkBussinessException e) {
			return failed(e.getMsgCode(), e.getParam());
		}
		return success(CommonConstants.MessageCode.SNU0001, notificationList, null);
	}
}
