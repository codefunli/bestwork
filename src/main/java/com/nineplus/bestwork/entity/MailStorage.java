package com.nineplus.bestwork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonStringType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "MailStorage")
@Data
@EqualsAndHashCode
@Table(name = "MAIL_STORAGE")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class MailStorage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private long id;

	@Column(name = "subject")
	private String subject;

	@Column(name = "recipient")
	private String recipient;

	@Column(name = "params", nullable = true, columnDefinition = "text")
	private String params;

}
