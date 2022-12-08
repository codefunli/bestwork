package com.nineplus.bestwork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "Nation")
@Table(name = "NATION_MASTER")
@Data
@EqualsAndHashCode
public class NationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "name", nullable = false)
	private String name;
}
