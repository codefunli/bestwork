package com.nineplus.bestwork.services;

import java.util.Optional;

import com.nineplus.bestwork.dto.IdsToDelReqDto;
import com.nineplus.bestwork.dto.NotificationReqDto;
import com.nineplus.bestwork.dto.NotificationResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.entity.NotificationEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

/**
 * 
 * @author DiepTT
 *
 */
public interface NotificationService {

	PageResDto<NotificationResDto> getAllNotifyByUser(PageSearchDto pageSearchDto) throws BestWorkBussinessException;

	Optional<NotificationEntity> findById(long notifId);

	NotificationEntity chgReadStatus(NotificationEntity notification) throws BestWorkBussinessException;

	void createNotification(NotificationReqDto notificationReqDto) throws BestWorkBussinessException;

	void deleteNotifyByIds(IdsToDelReqDto idsToDelReqDto) throws BestWorkBussinessException;

	long countNotReadNotifys() throws BestWorkBussinessException;

}
