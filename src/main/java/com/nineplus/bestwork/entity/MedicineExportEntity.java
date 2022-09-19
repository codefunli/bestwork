package com.nineplus.bestwork.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.nineplus.bestwork.model.ExportData;


@NamedNativeQuery(
	    name = "getExportData",
	    query = "select me.medicine_name, me.amount, me.medicine_company, m.manufacture_date, m.expire_date, m.origin, m.unit, m.cost, m.medicine_code, "
	            + "m.register_code, m.lot_code, m.criteria_manufacture, m.spec_package, m.concentration, m.usage_form "
	    		+ "from medicine_export me inner join medicine m on m.medicine_code = me.medicine_code and m.lot_code = me.lot_code "
	    		+ "where month(me.export_date) = :month",
	    resultSetMapping = "ExportData"
	)
	@SqlResultSetMapping(
	    name = "ExportData",
	    classes = @ConstructorResult(
	        targetClass = ExportData.class,
	        columns = {
	            @ColumnResult(name = "medicine_name", type = String.class),
	            @ColumnResult(name = "medicine_code", type = String.class),
	            @ColumnResult(name = "register_code", type = String.class),
	            @ColumnResult(name = "lot_code", type = String.class),
	            @ColumnResult(name = "criteria_manufacture", type = String.class),
	            @ColumnResult(name = "spec_package", type = String.class),
	            @ColumnResult(name = "concentration", type = String.class),
	            @ColumnResult(name = "usage_form", type = String.class),
	            @ColumnResult(name = "amount", type = Long.class),
	            @ColumnResult(name = "medicine_company", type = String.class),
	            @ColumnResult(name = "manufacture_date", type = Date.class),
	            @ColumnResult(name = "expire_date", type = Date.class),
	            @ColumnResult(name = "origin", type = String.class),
	            @ColumnResult(name = "unit", type = String.class),
	            @ColumnResult(name = "cost", type = BigDecimal.class)
	        }
	    )
	)

@Entity
@Table(name = "medicineExport")
public class MedicineExportEntity {

	private long id;
	private String medicineName;
	private String medicineCode;
	// Công ty sản xuất
	private String medicineCompany;
	private String lotCode;
	private Date exportDate;
	private long amount;
	public MedicineExportEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
      name = "sequence-generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
        @Parameter(name = "sequence_name", value = "user_sequence"),
        @Parameter(name = "initial_value", value = "1"),
        @Parameter(name = "increment_size", value = "1")
        }
    )
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "medicine_name", nullable = false)
	public String getMedicineName() {
		return medicineName;
	}
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}
	
	@Column(name = "medicine_code", nullable = false)
	public String getMedicineCode() {
		return medicineCode;
	}
	public void setMedicineCode(String medicineCode) {
		this.medicineCode = medicineCode;
	}
	
	@Column(name = "lot_code", nullable = false)
	public String getLotCode() {
		return lotCode;
	}
	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}
	
	@Column(name = "medicine_company", nullable = false)
	public String getMedicineCompany() {
		return medicineCompany;
	}
	public void setMedicineCompany(String medicineCompany) {
		this.medicineCompany = medicineCompany;
	}
	
	@Column(name = "export_date", nullable = false)
	public Date getExportDate() {
		return exportDate;
	}
	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}
	
	@Column(name = "amount", nullable = false)
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "MedicineExport [id=" + id + ", medicineName=" + medicineName + ", medicineCompany=" + medicineCompany
				+ ", exportDate=" + exportDate + ", amount=" + amount + "]";
	}
	
	
}
