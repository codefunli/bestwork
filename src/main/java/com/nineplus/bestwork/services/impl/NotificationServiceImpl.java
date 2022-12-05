package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.IdsToDelReqDto;
import com.nineplus.bestwork.dto.NotificationReqDto;
import com.nineplus.bestwork.dto.NotificationResDto;
import com.nineplus.bestwork.dto.PageResDto;
import com.nineplus.bestwork.dto.PageSearchDto;
import com.nineplus.bestwork.dto.RPageDto;
import com.nineplus.bestwork.entity.NotificationEntity;
import com.nineplus.bestwork.entity.UserEntity;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.NotificationRepository;
import com.nineplus.bestwork.services.NotificationService;
import com.nineplus.bestwork.services.UserService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.ConvertResponseUtils;
import com.nineplus.bestwork.utils.Enums.NotifyStatus;
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

	@Autowired
	private ConvertResponseUtils convertResponseUtils;

	/**
	 * @author DiepTT
	 * @param none
	 * @return list of notifications (response dto) per logged-in user
	 * @throws BestWorkBussinessException
	 */
	@Override
	public PageResDto<NotificationResDto> getAllNotifyByUser(PageSearchDto pageSearchDto)
			throws BestWorkBussinessException {
		UserAuthDetected userAuthDetected = userAuthUtils.getUserInfoFromReq(false);
		UserEntity curUser = userService.findUserByUsername(userAuthDetected.getUsername());
		try {
			Pageable pageable = convertSearch(pageSearchDto);

			Page<NotificationEntity> pageNotify = notifyRepository.findAllByUser(curUser.getId(), pageSearchDto,
					pageable);

			PageResDto<NotificationResDto> pageResDto = new PageResDto<>();
			RPageDto metaData = new RPageDto();
			metaData.setNumber(pageNotify.getNumber());
			metaData.setSize(pageNotify.getSize());
			metaData.setTotalElements(pageNotify.getTotalElements());
			metaData.setTotalPages(pageNotify.getTotalPages());
			pageResDto.setMetaData(metaData);

			List<NotificationResDto> dtos = new ArrayList<>();
			for (NotificationEntity noti : pageNotify.getContent()) {
				NotificationResDto dto = new NotificationResDto();
				dto.setId(noti.getId());
				dto.setTitle(noti.getTitle());
				dto.setContent(noti.getContent());
				dto.setCreateDate(String.valueOf(noti.getCreateDate()));
				dto.setRead(noti.isRead());
				dto.setUserId(noti.getUser().getId());
				dtos.add(dto);
			}
			pageResDto.setContent(dtos);
			return pageResDto;
		} catch (Exception ex) {
			throw new BestWorkBussinessException(ex.getMessage(), null);
		}
	}

	/**
	 * Private function: convert from PageSearchDto to Pageable and search condition
	 * 
	 * @param pageSearchDto
	 * @return Pageable
	 */
	private Pageable convertSearch(PageSearchDto pageSearchDto) {
		if (pageSearchDto.getKeyword().equals("")) {
			pageSearchDto.setKeyword("%%");
		} else {
			pageSearchDto.setKeyword("%" + pageSearchDto.getKeyword() + "%");
		}
		if (pageSearchDto.getStatus() < 0 || pageSearchDto.getStatus() >= NotifyStatus.values().length) {
			pageSearchDto.setStatus(-1);
		}
		String mappedColumn = convertResponseUtils.convertResponseNotify(pageSearchDto.getSortBy());
		return PageRequest.of(Integer.parseInt(pageSearchDto.getPage()), Integer.parseInt(pageSearchDto.getSize()),
				Sort.by(pageSearchDto.getSortDirection(), mappedColumn));
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
		notification.setRead(true);
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
			notification.setRead(false);
			notification.setCreateBy(getLoggedInUsername());
			notification.setUser(user);
			notifyRepository.save(notification);
		} else {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ECU0005, null);
		}
	}

	@Override
	public void deleteNotifyByIds(IdsToDelReqDto idsToDelReqDto) throws BestWorkBussinessException {
		UserAuthDetected userAuthDetected = userAuthUtils.getUserInfoFromReq(false);
		UserEntity curUser = userService.findUserByUsername(userAuthDetected.getUsername());

		Long[] ids = idsToDelReqDto.getListId();
		List<NotificationEntity> notifyList = new ArrayList<>();
		for (long id : ids) {
			Optional<NotificationEntity> notifyOpt = notifyRepository.findById(id);
			if (!notifyOpt.isPresent()) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.ENU0003, null);
			}
			notifyList.add(notifyOpt.get());
		}
		for (NotificationEntity notify : notifyList) {
			if (!chkCurUserCanDelNotify(notify, curUser)) {
				throw new BestWorkBussinessException(CommonConstants.MessageCode.E1X0014, null);
			}
		}
		if (notifyList.contains(null)) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.ENU0004, null);
		}
		this.notifyRepository.deleteAll(notifyList);
	}

	private boolean chkCurUserCanDelNotify(NotificationEntity notify, UserEntity curUser) {
		if (notify.getUser().equals(curUser)) {
			return true;
		}
		return false;
	}

	@Override
	public long countNotReadNotifys() throws BestWorkBussinessException {
		UserAuthDetected userAuthDetected = userAuthUtils.getUserInfoFromReq(false);
		UserEntity curUser = userService.findUserByUsername(userAuthDetected.getUsername());
		long count = this.notifyRepository.countUnreadNotify(curUser.getId());
		return count;
	}
}
