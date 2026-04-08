package com.ufrn.dtos;

public class QuestionResponseDTO {
	
	private String annotation;
	private Integer id;
	private Integer similarityLength;
	
	public QuestionResponseDTO(String annotation, Integer id, Integer similarityLength) {
		this.annotation = annotation;
		this.id = id;
		this.similarityLength = similarityLength;
	}

	public String getAnnotation() {
		return annotation;
	}
	
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSimilarityLength() {
		return similarityLength;
	}

	public void setSimilarityLength(Integer similarityLength) {
		this.similarityLength = similarityLength;
	}

}
