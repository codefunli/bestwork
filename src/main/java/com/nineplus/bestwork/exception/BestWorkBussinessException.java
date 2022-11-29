package com.nineplus.bestwork.exception;

import org.springframework.beans.factory.annotation.Autowired;

import com.nineplus.bestwork.utils.MessageUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BestWorkBussinessException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private MessageUtils messageUtils;

	private String msgCode;

	private Object param[];

	/**
	 * Constructor
	 * 
	 * @param String   msgCode
	 * @param Object[] param
	 */
	public BestWorkBussinessException(String msgCode, Object[] param) {
		this.msgCode = msgCode;
		this.param = param;
	}

}
