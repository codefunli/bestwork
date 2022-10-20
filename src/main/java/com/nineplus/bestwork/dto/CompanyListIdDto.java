package com.nineplus.bestwork.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CompanyListIdDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6197034371540394146L;
	private Long[] lstCompanyId;

}
