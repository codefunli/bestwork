package com.nineplus.bestwork.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PostInvoiceReqDto extends BaseDto {
	/**
	* 
	*/
	private static final long serialVersionUID = -1468801844948486874L;
	
	@JsonProperty("files")
	List<MultipartFile> mFiles;

	@JsonProperty("description")
	private String description;

	@JsonProperty("comment")
	private String comment;

}
