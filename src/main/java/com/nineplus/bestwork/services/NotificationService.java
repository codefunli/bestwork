package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.NotificationResDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface NotificationService {

	List<NotificationResDto> getAllNotificationsByUser() throws BestWorkBussinessException;

}
