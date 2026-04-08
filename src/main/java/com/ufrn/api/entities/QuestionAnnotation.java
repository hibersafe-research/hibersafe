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
@Table(name = "question_annotation")
public class QuestionAnnotation {
	
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_QUESTION_A")
	@SequenceGenerator(name="SEQ_QUESTION_A", sequenceName="id_seq_question_a", allocationSize=1)
    private long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Question question;
	
	@Column
	private String annotation;
	
	public QuestionAnnotation(Question question, String annotation) {
		this.question = question;
		this.annotation = annotation;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	
	
}
