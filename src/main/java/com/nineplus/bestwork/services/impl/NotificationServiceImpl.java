package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.dto.NotificationReqDto;
import com.nineplus.bestwork.dto.NotificationResDto;
import com.nineplus.bestwork.entity.NotificationEntity;
import com.nineplus.bestwork.entity.UserEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.NotificationRepository;
import com.nineplus.bestwork.services.NotificationService;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.UserAuthUtils;

/**
 * 
 * @author DiepTT
 *
 */
@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private UserAuthUtils userAuthUtils;

	@Autowired
	private UserService userService;

	@Override
	public List<NotificationResDto> getAllNotificationsByUser() throws BestWorkBussinessException {
		String username = getLoginningUsername();
		UserEntity currentUser = userService.findUserByUsername(username);
		List<NotificationResDto> dtoList = new ArrayList<>();
		List<NotificationEntity> notificationList = notificationRepository.findAllByUser(currentUser.getId());
		for (NotificationEntity noti : notificationList) {
			NotificationResDto dto = new NotificationResDto();
			dto.setId(noti.getId());
			dto.setTitle(noti.getTitle());
			dto.setContent(noti.getContent());
			dto.setCreateDate(noti.getCreateDate().toString());
			dto.setIsRead(noti.getIsRead());
			dto.setUserId(noti.getUser().getId());
			dtoList.add(dto);
		}
		return dtoList;
	}

	private String getLoginningUsername() throws BestWorkBussinessException {
		UserAuthDetected userAuthDetected = userAuthUtils.getUserInfoFromReq(false);
		String username = userAuthDetected.getUsername();
		return username;
	}

	@Override
	public Optional<NotificationEntity> findById(long notifId) {
		return this.notificationRepository.findById(notifId);
	}

	@Override
	public NotificationEntity changeNotificationReadingStatus(NotificationEntity notification)
			throws BestWorkBussinessException {
		notification.setIsRead(1);
		return this.notificationRepository.save(notification);
	}

	@Override
	public void createNotification(NotificationReqDto notificationReqDto) throws BestWorkBussinessException {
		UserEntity user = userService.findUserByUserId(notificationReqDto.getUserId());
		if (user != null) {
			NotificationEntity notification = new NotificationEntity();
			notification.setTitle(notificationReqDto.getTitle());
			notification.setContent(notificationReqDto.getContent());
			notification.setCreateDate(LocalDateTime.now());
			notification.setIsRead(0);
			notification.setCreateBy(getLoginningUsername());
			notification.setUser(user);
			notificationRepository.save(notification);
		} else {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0005, null);
		}
	}
}
