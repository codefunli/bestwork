package com.nineplus.bestwork.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity(name = "TCompany")
@Table(name = "T_COMPANY")
@Data
public class TCompany implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, precision = 19)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @CreationTimestamp
    @Column(name = "create_dt", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createDt;

    @UpdateTimestamp
    @Column(name = "update_dt", nullable = false)
    private LocalDateTime updateDt;

    @Column(name = "email")
    private String email;

    @Column(name = "tel_no")
    private String telNo;

    @Column(name = "tax_no")
    private String taxNo;
 
    @Column(name = "province_city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "ward")
    private String ward;

    @Column(name = "street")
    private String street;

    @Column(name = "detail_adress")
    private String detailAddress;
    
    // update registration
    @Column(name = "start_dt")
    private String startDate;

    @Column(name = "expired_dt")
    private String expiredDate;
    
    @Column(name = "is_expired")
    private boolean isExpired;
    
	/*
	 * @Override public boolean equals(Object o) { if (o == this) { return true; }
	 * if (!(o instanceof TCompany)) { return false; } TCompany other = (TCompany)
	 * o; boolean valueEquals = (this.id == null && other.id == null) || (this.id !=
	 * null && this.id.equals(other.id)); return valueEquals; }
	 * 
	 * @Override public int hashCode() { int hash = 7; hash = 31 * hash +
	 * id.intValue(); hash = 31 * hash + (companyName == null ? 0 :
	 * companyName.hashCode()); return hash; }
	 */
}
