package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private NotificationRepository notifyRepository;

	@Autowired
	private UserAuthUtils userAuthUtils;

	@Autowired
	private UserService userService;

	/**
	 * @author DiepTT
	 * @param none
	 * @return list of notifications (response dto) per logged-in user
	 * @throws BestWorkBussinessException
	 */
	@Override
	public List<NotificationResDto> getAllNotifyByUser() throws BestWorkBussinessException {
		String username = getLoggedInUsername();
		UserEntity currentUser = userService.findUserByUsername(username);
		List<NotificationResDto> dtoList = new ArrayList<>();
		List<NotificationEntity> notifyList = notifyRepository.findAllByUser(currentUser.getId());
		for (NotificationEntity noti : notifyList) {
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

	/**
	 * @author DiepTT
	 * @param none
	 * @return username of logged-in user
	 * @throws BestWorkBussinessException
	 */
	private String getLoggedInUsername() throws BestWorkBussinessException {
		UserAuthDetected userAuthDetected = userAuthUtils.getUserInfoFromReq(false);
		String username = userAuthDetected.getUsername();
		return username;
	}

	/**
	 * @author DiepTT
	 * @param notifyId (notification id)
	 * @return Optional<NotificationEntity>
	 * @throws none
	 */
	@Override
	public Optional<NotificationEntity> findById(long notifyId) {
		return this.notifyRepository.findById(notifyId);
	}

	/**
	 * This method is used to change reading-status of the notification.
	 * 
	 * @author DiepTT
	 * @param NotificationEntity
	 * @return notification that is already read
	 * @throws BestWorkBussinessException
	 */
	@Override
	public NotificationEntity chgReadStatus(NotificationEntity notification) throws BestWorkBussinessException {
		notification.setIsRead(1);
		return this.notifyRepository.save(notification);
	}

	/**
	 * This method is used to save notification into database.
	 * 
	 * @author DiepTT
	 * @param notification (request dto)
	 * @throws BestWorkBussinessException
	 */
	@Override
	@Transactional
	public void createNotification(NotificationReqDto notifyReqDto) throws BestWorkBussinessException {
		UserEntity user = userService.findUserByUserId(notifyReqDto.getUserId());
		if (user != null) {
			NotificationEntity notification = new NotificationEntity();
			String title = notifyReqDto.getTitle();
			String content = notifyReqDto.getContent();
			if (title.length() > CommonConstants.Character.STRING_LEN) {
				title = title.substring(0, CommonConstants.Character.STRING_LEN - 1);
			}
			if (content.length() > CommonConstants.Character.STRING_LEN) {
				content = content.substring(0, CommonConstants.Character.STRING_LEN - 1);
			}
			notification.setTitle(title);
			notification.setContent(content);
			notification.setCreateDate(LocalDateTime.now());
			notification.setIsRead(0);
			notification.setCreateBy(getLoggedInUsername());
			notification.setUser(user);
			notifyRepository.save(notification);
		} else {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0005, null);
		}
	}
}
