package com.nineplus.bestwork.entity;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "post_invoice")
@Entity(name = "PostInvoice")
@Getter
@Setter
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class PostInvoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 19)
	private long id;

	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "post_invoice_code")
	private String postInvoiceCode;

	@Column(name = "description")
	private String description;

	@Column(name = "comment")
	private String comment;

	@CreationTimestamp
	@Column(name = "create_date", nullable = false, insertable = false, updatable = false)
	private LocalDateTime createDate;

	@UpdateTimestamp
	@Column(name = "update_date")
	private LocalDateTime updateDate;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_by")
	private String updateBy;

	@Column(name = "airway_bill")
	private String airwayBill;
	
	@OneToMany(mappedBy = "postInvoice")
	private List<FileStorageEntity> fileStorages;
}
