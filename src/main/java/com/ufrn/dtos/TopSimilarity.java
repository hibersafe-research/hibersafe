package com.ufrn.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopSimilarity {
	
	@JsonProperty("annotation")
    private String annotation;
    
    @JsonProperty("url")
    private String url;

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TopSimilarity(String annotation, String url) {
		this.annotation = annotation;
		this.url = url;
	}
    
    

}
