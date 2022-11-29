package com.nineplus.bestwork.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportData {
	private String medicineName;
	private String medicineCode;
	private String registerCode;
	private String lotCode;
	private String criteriaManufacture;
	private String specPackage;
	private String concentration;
	private String usageForm;
	private Long amount;
	private String medicineCompany;
	private Date manufactureDate;
	private Date expireDate;
	private String origin;
	private String unit;
	private BigDecimal cost;

}
