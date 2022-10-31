package com.nineplus.bestwork.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity(name = "Progress")
@Table(name = "PROGRESS_TRACKING")
@Data
public class Progress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 19)
	private long id;

	@Column(name = "title")
	private String title;

	@Column(name = "status")
	private String status;

	@Column(name = "start_date")
	private String startDate;

	@Column(name = "end_date")
	private String endDate;

	@Column(name = "report")
	private String report;

	@Column(name = "note")
	private String note;
	
	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_by")
	private String updateBy;
	
	@Column(name = "create_date")
	LocalDateTime createDate;

	@Column(name = "update_date")
	LocalDateTime updateDate;

	@ManyToOne
	@JoinColumn(name = "project_id")
	@JsonIgnore
	private ProjectEntity project;

	@OneToMany(mappedBy = "progress")
	private List<FileStorageEntity> fileStorages;

}
