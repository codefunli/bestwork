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
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import lombok.Data;

@Entity(name = "TCompany")
@Table(name = "T_COMPANY")
@Data
@Indexed
public class TCompany implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 19)
	private Long id;

	@FullTextField()
	@Column(name = "company_name")
	private String companyName;

	@CreationTimestamp
	@Column(name = "create_date")
	private LocalDateTime createDt;

	@UpdateTimestamp
	@Column(name = "update_date")
	private LocalDateTime updateDt;

	@FullTextField()
	@Column(name = "email")
	private String email;

	@Column(name = "tel_no")
	private String telNo;

	@Column(name = "tax_no")
	private String taxNo;

	@FullTextField()
	@Column(name = "province_city")
	private String city;

	@FullTextField()
	@Column(name = "district")
	private String district;

	@FullTextField()
	@Column(name = "ward")
	private String ward;

	@FullTextField()
	@Column(name = "street")
	private String street;

	@Column(name = "start_date")
	private String startDate;

	@Column(name = "expired_date")
	private String expiredDate;

	@Column(name = "is_expired")
	private int isExpired;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_by")
	private String updateBy;

	@Column(name = "delete_flag")
	private int deleteFlag;

}
