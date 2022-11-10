package com.nineplus.bestwork.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nineplus.bestwork.dto.PostInvoiceReqDto;
import com.nineplus.bestwork.entity.PostInvoice;
import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;
import com.nineplus.bestwork.repository.PostInvoiceRepository;
import com.nineplus.bestwork.services.IPostInvoiceService;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.UserAuthUtils;

@Service
@Transactional
public class PostInvoiceServiceImpl implements IPostInvoiceService {

	@Autowired
	UserAuthUtils userAuthUtils;

	@Autowired
	PostInvoiceRepository postInvoiceRepository;

	@Override
	public PostInvoice savePostInvoice(PostInvoiceReqDto postInvoiceReqDto) throws BestWorkBussinessException {
		PostInvoice postInvoce = new PostInvoice();
		UserAuthDetected userAuthRoleReq = userAuthUtils.getUserInfoFromReq(false);
		try {
			postInvoce.setAirwayBill(postInvoiceReqDto.getAirway_bill());
			postInvoce.setDescription(postInvoiceReqDto.getDescription());
			postInvoce.setComment(postInvoiceReqDto.getComment());
			postInvoce.setCreateBy(userAuthRoleReq.getUsername());
			postInvoce.setUpdateBy(userAuthRoleReq.getUsername());
			postInvoce.setCreateDate(LocalDateTime.now());
			postInvoiceRepository.save(postInvoce);
			return postInvoiceRepository.save(postInvoce);
		} catch (Exception ex) {
			throw new BestWorkBussinessException(CommonConstants.MessageCode.eA0001, null);
		}

	}

}
