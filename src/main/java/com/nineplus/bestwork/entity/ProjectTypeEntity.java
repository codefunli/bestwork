package com.nineplus.bestwork.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;

@Entity(name = "ProjectTypeEntity")
@Data
@Table(name = "PROJECT_TYPE")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ProjectTypeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, columnDefinition = "smallint")
	private Integer id;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
	private String name;

	@Column(name = "description", nullable = false, columnDefinition = "text")
	private String description;

	@OneToMany(mappedBy = "projectType")
	@JsonBackReference
	private Collection<ProjectEntity> projects;
}
