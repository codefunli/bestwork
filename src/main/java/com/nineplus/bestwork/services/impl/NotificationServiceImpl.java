package com.nineplus.bestwork.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nineplus.bestwork.dto.NotificationResponseDto;
import com.nineplus.bestwork.entity.NotificationEntity;
import com.nineplus.bestwork.entity.TUser;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.NotificationRepository;
import com.nineplus.bestwork.services.NotificationService;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private UserAuthUtils userAuthUtils;

	@Autowired
	private UserService userService;

	@Override
	public List<NotificationResponseDto> getAllNotificationsByUser() throws BestWorkBussinessException {
		UserAuthDetected userAuthDetected = userAuthUtils.getUserInfoFromReq(false);
		String username = userAuthDetected.getUsername();
		TUser currentUser = userService.findUserByUsername(username);
		List<NotificationResponseDto> dtoList = new ArrayList<>();
		List<NotificationEntity> notificationList = notificationRepository.findAllByUser(currentUser.getId());
		for (NotificationEntity noti : notificationList) {
			NotificationResponseDto dto = new NotificationResponseDto();
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
}
