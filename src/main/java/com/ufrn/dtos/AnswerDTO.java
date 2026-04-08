package com.ufrn.dtos;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufrn.api.entities.Answer;
import com.ufrn.api.entities.Question;

public class AnswerDTO {

    @JsonProperty("answer_id")
    private Long answerId;

    @JsonProperty("is_accepted")
    private Boolean isAccepted;

    @JsonProperty("score")
    private int score;

    @JsonProperty("body")
    private String body;

    @JsonProperty("creation_date")
    private LocalDateTime creationDate;

    @JsonProperty("last_activity_date")
    private LocalDateTime lastActivityDate;
    
    public AnswerDTO() {
    	
    }
    
    public AnswerDTO(Answer answer) {
    	setId(answer.getId());
    	setAccepted(answer.isAccepted());
    	setScore(answer.getScore());
    	setBody(answer.getBody());
    	setCreationDateLocalDate(answer.getCreationDate());
    	setLastActivityDateLocalDate(answer.getLastActivityDate());
    }

    public Answer toAnswer(Question question) {
        return new Answer(answerId, isAccepted, score, body, question, creationDate, lastActivityDate);
    }

    public Long getId() {
        return answerId;
    }

    public void setId(Long answerId) {
        this.answerId = answerId;
    }

    public Boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = LocalDateTime.ofEpochSecond(creationDate, 0, ZoneOffset.UTC);
    }
    
    public void setCreationDateLocalDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(long lastActivityDate) {
        this.lastActivityDate = LocalDateTime.ofEpochSecond(lastActivityDate, 0, ZoneOffset.UTC);
    }
    
    public void setLastActivityDateLocalDate(LocalDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    @Override
    public String toString() {
        return "AnswerDTO { \\n\\tanswerId: " + answerId + ", \\n\\tbody: " + body + ", \\n\\tcreationDate: "
                + creationDate + ", \\n\\tisAccepted: " + isAccepted + ", \\n\\tlastActivityDate: " + lastActivityDate
                + ", \\n\\tscore: " + score + "}";
    }
}
