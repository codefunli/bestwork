package com.nineplus.bestwork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;

@Entity(name="ProjectEntity")
@Data
@Table(name = "T_PROJECT")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TProject {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, precision = 19)
	private long id;
	
	@Column(name = "prj_nm", nullable = false)
	private String prjName;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "prj_type", nullable = false)
	private String prjType;
	
	@Column(name = "important_f", nullable = false)
	private String isImportant;
	
	@Column(name = "consider_f", nullable = false)
	private String isConsider;
	
	@Column(name = "notification_f", nullable = false)
	private String notifFlag;
	
	@Column(name = "is_payed", nullable = false)
	private String isPay;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "rating", nullable = false)
	private String rating;
	
	@Column(name = "comment", nullable = false)
	private String comment;
}
