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
        return questionId + "\t" + similarity + "\n";
    }

    public String toRagContext(){
        return "";
    }
}
