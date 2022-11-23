package com.nineplus.bestwork.entity;

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
import org.hibernate.annotations.UpdateTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author TuanNA
 *
 */
@Table(name = "EVIDENCE_AFTER_POST")
@Entity(name = "EvidenceAfterPost")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EvidenceAfterPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, precision = 19)
	private long id;

	@Column(name = "description")
	private String description;

	@Column(name = "comment")
	private String comment;

	@Column(name = "airway_bill")
	private String airWayBill;

	@CreationTimestamp
	@Column(name = "create_date")
	private LocalDateTime createDate;

	@UpdateTimestamp
	@Column(name = "update_date")
	private LocalDateTime updateDate;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "update_by")
	private String updateBy;

	@OneToMany(mappedBy = "evidenceAfterPostId")
	private List<FileStorageEntity> fileStorages;

}
