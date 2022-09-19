package com.nineplus.bestwork.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportInput {
	private String medicineName;
	private String medicineCode;
	private String unit;
	private Date expireDate;
	private Long totalImport;
	private Long totalExport;
	private Long amountStartMonth;
	private Long amountEndMonth;
	
}
