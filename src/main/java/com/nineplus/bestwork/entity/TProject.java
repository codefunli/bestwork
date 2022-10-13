package com.nineplus.bestwork.entity;

import java.sql.Timestamp;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nineplus.bestwork.model.ProjectStatus;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ProjectEntity")
@Table(name = "T_PROJECT")
@TypeDef(name = "json", typeClass = JsonStringType.class)
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TProject {
	@Id
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "varchar(20)")
	private String id;

	@Column(name = "project_name", nullable = false, columnDefinition = "varchar(250)")
	private String projectName;

	@Column(name = "description", nullable = false, columnDefinition = "text")
	private String description;

	@Column(name = "project_type", nullable = true, columnDefinition = "smallint")
	private Integer projectType;

	@Column(name = "notification_flag", nullable = true, columnDefinition = "tinyint(1)")
	private Integer notificationFlag;

	@Column(name = "is_paid", nullable = true, columnDefinition = "tinyint(1)")
	private Integer isPaid;

	@Column(name = "status", nullable = false, columnDefinition = "tinyint(1)")
	private ProjectStatus status;

	@Column(name = "create_date", nullable = true, columnDefinition = "timestamp")
	private Timestamp createDate;

	@Column(name = "update_date", nullable = true, columnDefinition = "timestamp")
	private Timestamp updateDate;

	@Column(name = "comment", nullable = true, columnDefinition = "text")
	private String comment;

	@OneToMany(mappedBy = "project")
	@JsonBackReference
	private Collection<TFileStorage> fileStorages;

}
