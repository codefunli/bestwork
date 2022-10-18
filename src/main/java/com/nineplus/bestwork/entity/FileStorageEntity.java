package com.nineplus.bestwork.entity;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;

@Entity(name = "FileStorageEntity")
@Data
@Table(name = "FILE_STORAGE")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class FileStorageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name = "name", nullable = false)
	private String name;

	@Lob
	@Column(name = "data", nullable = false)
	private byte[] data;

	@Column(name = "type", nullable = true, columnDefinition = "varchar(50)")
	private String type;

	@Column(name = "create_date", nullable = true)
	private Timestamp createDate;

//	@Column(name = "update_date", nullable = true)
//	private Timestamp updateDate;

	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private PostEntity post;

	public FileStorageEntity(String name, byte[] data, String type) {
		super();
		this.name = name;
		this.data = data;
		this.type = type;
	}

	public FileStorageEntity(String name, byte[] data, String type, Timestamp createDate, PostEntity post) {
		super();
		this.name = name;
		this.data = data;
		this.type = type;
		this.createDate = createDate;
		this.post = post;
	}
	public FileStorageEntity() {
		super();
	}
}
