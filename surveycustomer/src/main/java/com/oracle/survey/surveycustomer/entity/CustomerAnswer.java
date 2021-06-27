package com.oracle.survey.surveycustomer.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "CustomerAnswer")
public class CustomerAnswer extends BaseEntity {
	@Id
	private String id;
	private String userId;
	private String surveyCode;
	private Long surveyVersion;
	@Lob
	private String userAnswerInfo;
	private Boolean isComplete;

	private Boolean isFirstTime;
	
	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSurveyCode() {
		return surveyCode;
	}

	public void setSurveyCode(String surveyCode) {
		this.surveyCode = surveyCode;
	}

	public Long getSurveyVersion() {
		return surveyVersion;
	}

	public void setSurveyVersion(Long surveyVersion) {
		this.surveyVersion = surveyVersion;
	}

	public String getUserAnswerInfo() {
		return userAnswerInfo;
	}

	public void setUserAnswerInfo(String userAnswerInfo) {
		this.userAnswerInfo = userAnswerInfo;
	}

	public Boolean getIsFirstTime() {
		return isFirstTime;
	}

	public void setIsFirstTime(Boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

}
