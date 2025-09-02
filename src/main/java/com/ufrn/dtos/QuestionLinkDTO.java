package com.ufrn.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class QuestionLinkDTO {
    @JsonProperty("question_id")
    private Long questionId;

    @JsonProperty("similarity")
    private Double similarity;

    public String toRagContext(){
        return "";
    }
}
