package com.ufrn.dtos;

import java.util.List;

public class ResponseDTO {
	
	List<TopSimilarity> topSimilarity;
	List<AllResults> allResults;
	
	public ResponseDTO(List<TopSimilarity> topSimilarity, List<AllResults> allResults) {
		this.topSimilarity = topSimilarity;
		this.allResults = allResults;
	}

	public List<TopSimilarity> getTopSimilarity() {
		return topSimilarity;
	}
	
	public void setTopSimilarity(List<TopSimilarity> topSimilarity) {
		this.topSimilarity = topSimilarity;
	}
	
	public List<AllResults> getReturnDTO() {
		return allResults;
	}
	
	public void setReturnDTO(List<AllResults> allResults) {
		this.allResults = allResults;
	}
	
	

}
