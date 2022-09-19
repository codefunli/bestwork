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

import com.nineplus.bestwork.model.ReportInput;

import lombok.Data;

@Entity
@NamedNativeQuery(
    name = "getReportData",
    query = "SELECT m.medicine_name,m.medicine_code, m.unit, m.expire_date, m.amount total_import, tx.amount total_export, m.amount amount_start_month, (m.amount - tx.amount) amount_end_month "
    		+ "FROM (SELECT m1.medicine_name,m1.medicine_code,m1.unit, m1.expire_date, SUM(m1.amount) amount FROM medicine m1 WHERE MONTH(m1.import_date) = :month GROUP BY m1.medicine_name) as m "
    		+ "JOIN (SELECT m3.medicine_name, SUM(m3.amount) amount FROM medicine_export m3 WHERE MONTH(m3.export_date) = :month GROUP BY m3.medicine_name) as tx ON m.medicine_name = tx.medicine_name",
    resultSetMapping = "ReportInput"
)
@SqlResultSetMapping(
    name = "ReportInput",
    classes = @ConstructorResult(
        targetClass = ReportInput.class,
        columns = {
            @ColumnResult(name = "medicine_name", type = String.class),
            @ColumnResult(name = "medicine_code", type = String.class),
            @ColumnResult(name = "unit", type = String.class),
            @ColumnResult(name = "expire_date", type = Date.class),
            @ColumnResult(name = "total_import", type = Long.class),
            @ColumnResult(name = "total_export", type = Long.class),
            @ColumnResult(name = "amount_start_month", type = Long.class),
            @ColumnResult(name = "amount_end_month", type = Long.class)
        }
    )
)
@Data
@Table(name = "medicine")
public class MedicineEntity {
	private long id;
	private String medicineName;
	// Công ty sản xuất
	private String medicineCompany;
	// Đơn giá
	private BigDecimal cost;
	// Mã số thuốc
	private String medicineCode;
	// Số đăng ký
	private String registerCode;
	// Số lô
	private String lotCode;
	
	private String criteriaManufacture;
	
	private String specPackage;
	
	private String concentration;
	
	private String usageForm;
	// Xuất xứ
	private String origin;
	// Ngày sản xuất
	private Date manufactureDate;
	private Date expireDate;
	private long amount;
	private String unit;
	private Date importDate;

	public MedicineEntity() {
		
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
	
	@Column(name = "medicine_code", nullable = false)
	public String getMedicineCode() {
		return this.medicineCode;
	}
	
	public void setMedicineCode(String medicineCode) {
		this.medicineCode = medicineCode;
	}
	
	@Column(name = "register_code", nullable = false)
	public String getRegisterCode() {
		return this.registerCode;
	}
	
	public void setRegisterCode(String registerCode) {
		this.registerCode = registerCode;
	}

	@Column(name = "medicine_name", nullable = false)
	public String getMedicineName() {
		return this.medicineName;
	}

	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}
	
	@Column(name = "lot_code", nullable = false)
	public String getLotCode() {
		return this.lotCode;
	}
	
	public void setLotCode(String lotCode) {
		this.lotCode = lotCode;
	}
	
	@Column(name = "criteria_manufacture", nullable = false)
	public String getCriteriaManufacture() {
		return this.criteriaManufacture;
	}

	public void setCriteriaManufacture(String criteriaManufacture) {
		this.criteriaManufacture = criteriaManufacture;
	}
	
	@Column(name = "spec_package", nullable = false)
	public String getSpecPackage() {
		return this.specPackage;
	}

	public void setSpecPackage(String specPackage) {
		this.specPackage = specPackage;
	}
	
	@Column(name = "concentration", nullable = false)
	public String getConcentration() {
		return this.concentration;
	}

	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}
	
	@Column(name = "usage_form", nullable = false)
	public String getUsageForm() {
		return this.usageForm;
	}

	public void setUsageForm(String usageForm) {
		this.usageForm = usageForm;
	}

	@Column(name = "medicine_company", nullable = false)
	public String getMedicineCompany() {
		return medicineCompany;
	}

	public void setMedicineCompany(String medicineCompany) {
		this.medicineCompany = medicineCompany;
	}
	@Column(name = "origin", nullable = false)
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name = "manufacture_date", nullable = false)
	public Date getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	@Column(name = "expire_date", nullable = false)
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	@Column(name = "amount", nullable = false)
	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	@Column(name = "unit", nullable = false)
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	@Column(name = "import_date", nullable = false)
	public Date getImportDate() {
		return importDate;
	}

	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	
	@Column(name = "cost", nullable = false)
	public BigDecimal getCost() {
		return this.cost;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Medicine [id=" + id + ", medicineName=" + medicineName + ", medicineCompany=" + medicineCompany
				+ ", origin=" + origin + ", manufactureDate=" + manufactureDate + ", expireDate=" + expireDate
				+ ", amount=" + amount + ", unit=" + unit + "]";
	}

	
	
}
