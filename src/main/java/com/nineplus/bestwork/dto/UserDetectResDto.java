package com.nineplus.bestwork.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nineplus.bestwork.entity.CompanyEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserDetectResDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7843348960500696622L;
	
	@JsonProperty("id")
    private long id;

	@JsonProperty("userName")
    private String userName;

	@JsonProperty("firstName")
    private String firstName;

	@JsonProperty("lastName")
    private String lastName;

	@JsonProperty("uEmail")
    private String email;

	@JsonProperty("enabled")
    private int isEnable;

	@JsonProperty("uTelNo")
    private String telNo;

	@JsonProperty("uRole")
    private String role;

    @JsonProperty("loginFailedNum")
    private String loginFailedNum;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("company")
    CompanyResDto company;

    @JsonProperty("project")
    List<UserWithProjectResDto> roleProject;

    @JsonProperty("permissions")
    Map<Long, List<PermissionResDto>> permissions;
}
