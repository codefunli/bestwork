package com.nineplus.bestwork.controller;

import java.util.ArrayList;
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

import com.nineplus.bestwork.dto.IdsToDelReqDto;
import com.nineplus.bestwork.dto.NotificationResDto;
import com.nineplus.bestwork.dto.NotifyStatusResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.entity.NotificationEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.services.NotificationService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.Enums.NotifyStatus;

/**
 * 
 * @author DiepTT
 *
 */
@RestController
@RequestMapping(value = "api/v1/notifications")
public class NotificationController extends BaseController {

	@Autowired
	private NotificationService notifyService;

	@PostMapping("/list")
	public ResponseEntity<? extends Object> getAllNotifyByUser(@RequestBody PageSearchDto pageSearchDto) {
		PageResDto<NotificationResDto> pageNotify = null;
		try {
			pageNotify = notifyService.getAllNotifyByUser(pageSearchDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SNU0001, pageNotify, null);
	}

	@PatchMapping("/read/{notifId}")
	public ResponseEntity<? extends Object> changeNotificationReadingStatus(@PathVariable long notifId) {

		Optional<NotificationEntity> notificationOpt = notifyService.findById(notifId);
		if (notificationOpt.isEmpty()) {
			return failed(CommonConstants.MessageCode.ENU0004, null);
		}
		NotificationEntity notification = notificationOpt.get();
		try {
			notification = notifyService.chgReadStatus(notification);
		} catch (BestWorkBussinessException e) {
			return failed(e.getMsgCode(), e.getParam());
		}
		return success(CommonConstants.MessageCode.SNU0005, notification, null);
	}

	@PostMapping("/delete")
	public ResponseEntity<? extends Object> deleteNotify(@RequestBody IdsToDelReqDto idsToDelReqDto) {
		try {
			notifyService.deleteNotifyByIds(idsToDelReqDto);
		} catch (BestWorkBussinessException ex) {
			return failed(ex.getMsgCode(), ex.getParam());
		}
		return success(CommonConstants.MessageCode.SNU0007, null, null);
	}

	/**
	 * Function: get list of notification status (is read/not read)
	 * 
	 * @return (ResponseEntity<apiResponseDto>) message and list of notification
	 *         status
	 * @throws BestWorkBussinessException
	 */
	@GetMapping("/status")
	public ResponseEntity<? extends Object> getNotifyStatus() throws BestWorkBussinessException {
		List<NotifyStatusResDto> notifyStatus = new ArrayList<>();
		for (NotifyStatus status : NotifyStatus.values()) {
			NotifyStatusResDto dto = new NotifyStatusResDto();
			dto.setId(status.ordinal());
			dto.setStatus(status.getValue());
			notifyStatus.add(dto);
		}
		return success(CommonConstants.MessageCode.SNU0006, notifyStatus, null);
	}

	@GetMapping("/count-not-read")
	public ResponseEntity<? extends Object> getQuanOfNotReadNotify() throws BestWorkBussinessException {
		long countNotRead = this.notifyService.countNotReadNotifys();
		return success(CommonConstants.MessageCode.SNU0008, countNotRead, null);
	}
}
