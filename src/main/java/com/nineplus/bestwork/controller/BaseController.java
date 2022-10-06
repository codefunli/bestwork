package com.nineplus.bestwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nineplus.bestwork.dto.ApiResponseDTO;
import com.nineplus.bestwork.utils.CommonConstants;
import com.nineplus.bestwork.utils.MessageUtils;
/**
 * Custom for response API object with 4 fields : code, message, data, status
 * 
 * @author tuanna
 *
 */
public abstract class BaseController {
	@Autowired
	MessageUtils messageUtils;

	/**
	 * 
	 * @param msgCode
	 * @param data
	 * @param params
	 * @return object DTO when process successfully
	 */
	public ResponseEntity<? extends Object> success(String msgCode, Object data, Object[] params) {

		ApiResponseDTO apiResponseDto = ApiResponseDTO.builder().code(msgCode)
				.message(messageUtils.getMessage(msgCode, params)).data(data)
				.status(CommonConstants.ApiStatus.STATUS_OK).build();

		return new ResponseEntity<ApiResponseDTO>(apiResponseDto, HttpStatus.OK);
	}

	/**
	 * 
	 * @param msgCode
	 * @param params
	 * @return object DTO when process failed
	 */
	public ResponseEntity<? extends Object> failed(String msgCode, Object[] params) {

		ApiResponseDTO apiResponseDto = ApiResponseDTO.builder().code(msgCode)
				.message(messageUtils.getMessage(msgCode, params)).data(null)
				.status(CommonConstants.ApiStatus.STATUS_ERROR).build();

		return new ResponseEntity<ApiResponseDTO>(apiResponseDto, HttpStatus.OK);
	}
	
	public ResponseEntity<? extends Object> failedWithError(String msgCode, Object data, Object[] params) {
		
		ApiResponseDTO apiResponseDto = ApiResponseDTO.builder().code(msgCode)
				.message(messageUtils.getMessage(msgCode, params)).data(data)
				.status(CommonConstants.ApiStatus.STATUS_ERROR).build();
		
		return new ResponseEntity<ApiResponseDTO>(apiResponseDto, HttpStatus.OK);
	}

}
