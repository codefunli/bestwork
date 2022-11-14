package com.nineplus.bestwork.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author DiepTT
 *
 */
@Entity
@Data
@EqualsAndHashCode
@Table(name = "NOTIFICATION")
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, name = "id")
	private long id;

	@Column(name = "title", nullable = false, columnDefinition = "varchar(255)")
	private String title;

	@Column(name = "content", nullable = false, columnDefinition = "varchar(255)")
	private String content;

	@Column(name = "create_date", columnDefinition = "timestamp")
	private LocalDateTime createDate;

	@Column(name = "is_read", nullable = false, columnDefinition = "tinyint(1) default 0")
	private int isRead;
	
	@Column(name = "create_by", nullable = false, columnDefinition = "varchar(255)")
	private String createBy;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, columnDefinition = "long")
	private UserEntity user;

}
