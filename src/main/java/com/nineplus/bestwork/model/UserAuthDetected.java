package com.nineplus.bestwork.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserAuthDetected {
	private Boolean isSysAdmin;
	private Boolean isOrgUser;
	private Boolean isOrgAdmin;
	private String username;
	private List<String> roles;
}
