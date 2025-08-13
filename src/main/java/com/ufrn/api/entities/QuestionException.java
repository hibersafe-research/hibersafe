package com.ufrn.api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "question_exception")
public class QuestionException {
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_QUESTION_E")
	@SequenceGenerator(name="SEQ_QUESTION_E", sequenceName="id_seq_question_e", allocationSize=1)
    private long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Question question;
	
	@Column
	private String exception;
	
	public QuestionException(Question question, String exception) {
		this.question = question;
		this.exception = exception;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
	
	
}
