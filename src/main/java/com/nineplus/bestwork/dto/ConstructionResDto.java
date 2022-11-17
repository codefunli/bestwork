package com.nineplus.bestwork.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */
@Data
@EqualsAndHashCode
public class ConstructionResDto {

	private long id;

	private String constructionName;

	private String description;

	private String startDate;

	private String endDate;

	private String location;

	private String createBy;

	private String status;

	private String projectCode;

	private List<String> awbCodes;
}
