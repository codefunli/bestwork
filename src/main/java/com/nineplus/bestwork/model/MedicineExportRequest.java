package com.nineplus.bestwork.model;

import java.util.Date;

public class MedicineExportRequest {

	private String medicineCode;
	private String lotCode;
	private long amount;
	private Date exportDate;

	public MedicineExportRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMedicineCode() {
		return medicineCode;
	}

	public void setMedicineCode(String medicineCode) {
		this.medicineCode = medicineCode;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public Date getExportDate() {
		return exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	public String getLotCode() {
		return lotCode;
	}

	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}

}
