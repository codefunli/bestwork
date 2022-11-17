package com.nineplus.bestwork.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserAuthDetected {
	private Boolean isSysAdmin;
	private Boolean isOrgAdmin;
	private Boolean isOrgUser;
	private Boolean isInvestor;
	private Boolean isSupplier;
	private Boolean isContractor;

	private String username;
	private List<String> roles;
}
