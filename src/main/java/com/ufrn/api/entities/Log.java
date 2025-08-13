package com.ufrn.api.entities;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "log")
public class Log {
	
	@Id
	@GeneratedValue
	public Integer id;
	
	@Column
	public Calendar date;
	
	@Column(columnDefinition = "TEXT")
	public String questionIds;
	
	@Column
	public Integer countQuestionIds;
	
	@Column
	public String annotation;
	
	@Column
	public String exception;
	
	@Column(columnDefinition="text")
	public String message;
	
	public Log() {
		
	}

	public Log(Calendar date, String questionIds, Integer countQuestionIds, String annotation, String exception, String message) {
		this.date = date;
		this.questionIds = questionIds;
		this.countQuestionIds = countQuestionIds;
		this.annotation = annotation;
		this.exception = exception;
		this.message = message;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(String questionIds) {
		this.questionIds = questionIds;
	}

	public Integer getCountQuestionIds() {
		return countQuestionIds;
	}

	public void setCountQuestionIds(Integer countQuestionIds) {
		this.countQuestionIds = countQuestionIds;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", date=" + date + ", questionIds=" + questionIds + ", countQuestionIds="
				+ countQuestionIds + ", annotation=" + annotation + ", exception=" + exception + ", message=" + message
				+ "]";
	}

}
