package com.nineplus.bestwork.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;

/**
 * 
 * @author DiepTT
 *
 */

@Entity(name = "PostEntity")
@Data
@Table(name = "POST")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class PostEntity {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", columnDefinition = "VARCHAR(16)")
	private String id;

	@Column(name = "description", nullable = true, columnDefinition = "VARCHAR(255)")
	private String description;

	@Column(name = "create_date", nullable = true)
	private Timestamp createDate;

	@ManyToOne
	@JoinColumn(name = "project_id")
	@JsonIgnore
	private ProjectEntity project;

	@OneToMany(mappedBy = "post")
	private List<FileStorageEntity> fileStorages;

	@Column(name="comment", nullable = true, columnDefinition = "text")
	private String comment;
	
}
