package com.nineplus.bestwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TProjectResponseDto {
	

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7969015153524943561L;
    
	@JsonProperty("prjId")
	private long prjId;
	
	@JsonProperty("prjName")
	private String prjName;
	
	@JsonProperty("title")
	private String title;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("prjType")
	private String prjType;
	
	@JsonProperty("isImportant")
	private String isImportant;
	
	@JsonProperty("isConsider")
	private String isConsider;
	
	@JsonProperty("notifFlag")
	private String notifFlag;
	
	@JsonProperty("isPayed")
	private String isPayed;

	@JsonProperty("status")
	private String status;
	
	@JsonProperty("rating")
	private String rating;
	
	@JsonProperty("comment")
	private String comment;

}
