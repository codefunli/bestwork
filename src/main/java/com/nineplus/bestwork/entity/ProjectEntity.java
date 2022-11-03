package com.nineplus.bestwork.entity;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author DiepTT
 *
 */

@Entity(name = "ProjectEntity")
@Table(name = "PROJECT")
@TypeDef(name = "json", typeClass = JsonStringType.class)
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntity {
	@Id
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "varchar(20)")
	private String id;

	@Column(name = "project_name", nullable = false, columnDefinition = "varchar(250)")
	private String projectName;

	@Column(name = "description", nullable = false, columnDefinition = "text")
	private String description;

	@Column(name = "notification_flag", nullable = true, columnDefinition = "tinyint(1)")
	private Boolean notificationFlag;

	@Column(name = "is_paid", nullable = true, columnDefinition = "tinyint(1)")
	private Boolean isPaid;

	@Column(name = "status", nullable = false, columnDefinition = "tinyint(1)")
	private String status;

	@Column(name = "start_date", nullable = false)
	private String startDate;
	
	@Column(name = "create_by")
	private String createBy;
	
	@Column(name = "update_by")
	private String updateBy;
	
	@Column(name = "create_date", nullable = false, insertable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createDate;

	@Column(name = "update_date", nullable = false)
	@UpdateTimestamp
	private LocalDateTime updateDate;

	@ManyToOne
	@JoinColumn(name = "project_type")
	private ProjectTypeEntity projectType;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
	@JsonBackReference
	private Collection<PostEntity> posts;

}
