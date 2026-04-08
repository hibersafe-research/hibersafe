package com.ufrn.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AllResults {
	
	@JsonProperty("annotation")
    private String annotation;
	
	@JsonProperty("count")
    private Integer count;
    
    @JsonProperty("urls")
    private List<String> urls;

	public AllResults() {
	}

	public AllResults(String annotation, Integer count, List<String> urls) {
		this.annotation = annotation;
		this.count = count;
		this.urls = urls;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
