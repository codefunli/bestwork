package com.nineplus.bestwork.services;

import java.util.List;
import java.util.Optional;

import com.nineplus.bestwork.dto.NotificationReqDto;
import com.nineplus.bestwork.dto.NotificationResDto;
import com.nineplus.bestwork.entity.NotificationEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
/**
 * 
 * @author DiepTT
 *
 */
public interface NotificationService {

	List<NotificationResDto> getAllNotifyByUser() throws BestWorkBussinessException;

	Optional<NotificationEntity> findById(long notifId);

	NotificationEntity chgNotifyReadStatus(NotificationEntity notification)
			throws BestWorkBussinessException;

	void createNotification(NotificationReqDto notificationReqDto) throws BestWorkBussinessException;
}
