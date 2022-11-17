package com.nineplus.bestwork.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */
@Entity
@Data
@EqualsAndHashCode
@Table(name = "CONSTRUCTION")
public class ConstructionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = true)
	private String description;

	@Column(name = "start_date", nullable = true)
	private String startDate;

	@Column(name = "end_date", nullable = true)
	private String endDate;

	@Column(name = "location", nullable = true)
	private String location;

	@Column(name = "create_by", nullable = true)
	private String createBy;

	@Column(name = "status", nullable = false)
	private String status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "AWB_CONSTRUCTION", joinColumns = @JoinColumn(name = "construction_id"), inverseJoinColumns = @JoinColumn(name = "awb_id"))
	List<AirWayBill> airWayBills;
}
