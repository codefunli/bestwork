package com.nineplus.bestwork.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;

@Entity(name = "PostEntity")
@Data
@Table(name = "T_POST")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "create_date", nullable = true)
	private Timestamp createDate;

	@ManyToOne
	@JoinColumn(name = "project_id")
	@JsonIgnore
	private TProject project;

}
