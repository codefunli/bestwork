package com.nineplus.bestwork.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectAssignReqDto extends BaseDto {

	private static final long serialVersionUID = 205181823126677365L;

	@JsonProperty("companyId")
	private long companyId;

	@JsonProperty("companyName")
	private String companyName;

	@JsonProperty("userList")
	private List<ProjectRoleUserReqDto> userList;

}
