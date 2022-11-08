package com.nineplus.bestwork.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nineplus.bestwork.dto.NotificationReqDto;
import com.nineplus.bestwork.dto.NotificationResDto;
import com.nineplus.bestwork.entity.NotificationEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.NotificationService;
import com.nineplus.bestwork.utils.CommonConstants;
/**
 * 
 * @author DiepTT
 *
 */
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

	@PatchMapping("/read/{notifId}")
	public ResponseEntity<? extends Object> changeNotificationReadingStatus(@PathVariable long notifId) {

		Optional<NotificationEntity> notificationOpt = notificationService.findById(notifId);
		if (notificationOpt.isEmpty()) {
			return failed(CommonConstants.MessageCode.ENU0004, null);
		}
		NotificationEntity notification = notificationOpt.get();
		try {
			notification = notificationService.changeNotificationReadingStatus(notification);
		} catch (BestWorkBussinessException e) {
			return failed(e.getMsgCode(), e.getParam());
		}
		return success(CommonConstants.MessageCode.SNU0005, notification, null);
	}

	
	//This method is used to test on Postman, later it will be used through notificationService when in need  
	@PostMapping("/create")
	public ResponseEntity<? extends Object> createNotifications(@RequestBody NotificationReqDto notificationReqDto) {
		try {
			notificationService.createNotification(notificationReqDto);
		} catch (BestWorkBussinessException e) {
			return failed(e.getMsgCode(), e.getParam());
		}
		return success(CommonConstants.MessageCode.SNU0002, null, null);
	}
}
