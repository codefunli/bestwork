package com.nineplus.bestwork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity(name = "AssignTask")
@Table(name = "ASSIGN_TASK")
@Data
public class AssignTaskEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 19)
	private long id;
	
	@Column(name = "user_id")
	private long userId;

	@Column(name = "company_id")
	private long companyId;

	@Column(name = "project_id")
	private String  projectId;

	@Column(name = "can_view")
	private boolean canView;

	@Column(name = "can_edit")
	private boolean canEdit;

}
