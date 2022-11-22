package com.nineplus.bestwork.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
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
	@Column(name = "id", nullable = false)
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Lob
	@Column(name = "data", nullable = false)
	private byte[] data;

	@Column(name = "type", nullable = true, columnDefinition = "varchar(50)")
	private String type;

	@CreationTimestamp
	@Column(name = "create_date")
	private Timestamp createDate;

	@Column(name = "path_file_server")
	private String pathFileServer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = true)
	@JsonIgnore
	private PostEntity post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "progress_id", nullable = true)
	@JsonIgnore
	private ProgressEntity progress;

	@Column(name = "post_invoice_id", nullable = true)
	private long postInvoiceId;

	@Column(name = "post_package_id", nullable = true)
	private long packagePostId;
	
	@Column(name = "construction_id", nullable = true)
	private long constructionId;

	@Column(name = "is_choosen")
	private boolean isChoosen;

}
