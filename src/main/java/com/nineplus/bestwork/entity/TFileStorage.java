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
@Table(name = "T_FILE_STORAGE")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TFileStorage {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "name", nullable = false)
	private String name;

	@Lob
	@Column(name = "data", nullable = false)
	private byte[] data;

	@Column(name = "type", nullable = true, columnDefinition = "varchar(50)")
	private String type;

	@Column(name = "create_date", nullable = true)
	private Timestamp createDate;

	@Column(name = "update_date", nullable = true)
	private Timestamp updateDate;

	@ManyToOne
	@JoinColumn(name = "post_id")
	@JsonIgnore
	private TPost post;

	public TFileStorage(String name, byte[] data, String type) {
		super();
		this.name = name;
		this.data = data;
		this.type = type;
	}

	public TFileStorage(String name, byte[] data, String type, Timestamp createDate) {
		super();
		this.name = name;
		this.data = data;
		this.type = type;
		this.createDate = createDate;;
	}
	public TFileStorage() {
		super();
	}
}
