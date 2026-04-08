package com.ufrn.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class QuestionLinkDTO {
    @JsonProperty("question_id")
    private Long questionId;

    @JsonProperty("similarity")
    private Double similarity;

    public QuestionLinkDTO(Long questionId, Double similarity) {
        this.questionId = questionId;
        this.similarity = similarity;
    }

    public String toString(){
        return "https://stackoverflow.com/questions/" + questionId + "\nsimilarity: " + similarity + "\n";
    }

    public String toRagContext(){
        return "https://stackoverflow.com/questions/" + questionId;
    }
}
