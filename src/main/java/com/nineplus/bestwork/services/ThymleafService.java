package com.nineplus.bestwork.services;

import com.nineplus.bestwork.dto.CompanyUserReqDto;

public interface ThymleafService {
	public String getContentMailResetPassword(String username, String link);
	public String getContentMailRegisterUserCompany(CompanyUserReqDto companyUserReqDto, String linkLogin);
}
