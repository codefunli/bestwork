package com.nineplus.bestwork.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nineplus.bestwork.exception.BestWorkBussinessException;
import com.nineplus.bestwork.model.UserAuthDetected;

@Component
public class UserAuthUtils {

	public UserAuthDetected getUserInfoFromReq(Boolean getOtherInfo) throws BestWorkBussinessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String userName = authentication.getName();
		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		Boolean isSysAdmin = roles.contains(Enums.TRole.SYS_ADMIN.getValue());
		Boolean isOrgAdmin = roles.contains(Enums.TRole.COMPANY_ADMIN.getValue());
		Boolean isInvestor = roles.contains(Enums.TRole.INVESTOR.getValue());
		Boolean isSupplier = roles.contains(Enums.TRole.SUPPLIER.getValue());
		Boolean isContractor = roles.contains(Enums.TRole.CONTRACTOR.getValue());

		UserAuthDetected userAuthDetected = new UserAuthDetected();
		userAuthDetected.setUsername(userName);
		userAuthDetected.setIsSysAdmin(isSysAdmin);
		userAuthDetected.setIsOrgAdmin(isOrgAdmin);
		userAuthDetected.setIsInvestor(isInvestor);
		userAuthDetected.setIsSupplier(isSupplier);
		userAuthDetected.setIsContractor(isContractor);
		userAuthDetected.setRoles(roles);

		return userAuthDetected;
	}

}
