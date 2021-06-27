package com.oracle.survey.surveyadmin.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

	private char deleted = 'N';

	public char getDeleted() {
		return deleted;
	}

	public void setDeleted(char deleted) {
		this.deleted = deleted;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	private Timestamp createdDate = Timestamp.valueOf(LocalDateTime.now());

	private Timestamp updatedDate = Timestamp.valueOf(LocalDateTime.now());

	private String createdBy = "System";

}
