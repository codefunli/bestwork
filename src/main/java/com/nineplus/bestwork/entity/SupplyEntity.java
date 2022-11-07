package com.nineplus.bestwork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity(name = "Supply")
@Table(name = "SUPPLIES")
@Data
public class SupplyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 19)
	private long id;
	
	@Column(name = "project_id")
	private String  projectId;

	@Column(name = "supply_name")
	private String supplyName;

	@Column(name = "eq_bill")
	private boolean eqBill;
	
	@Column(name = "received_date")
	private String receivedDate;

	@Column(name = "received_status")
	private int receivedStatus;

	@Column(name = "supply_status")
	private int supplyStatus;
	
	@Column(name = "feedback")
	private String feedback;

}
