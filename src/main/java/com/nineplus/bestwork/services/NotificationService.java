package com.nineplus.bestwork.services;

import java.util.List;

import com.nineplus.bestwork.dto.NotificationResponseDto;
import com.nineplus.bestwork.exception.BestWorkBussinessException;

public interface NotificationService {

	List<NotificationResponseDto> getAllNotificationsByUser() throws BestWorkBussinessException;

}
