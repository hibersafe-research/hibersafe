package com.ufrn.api.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.ufrn.utils.VectorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "question")
public class Question {
    
	@Id
    private Long id;

    @Column(name = "embedding", columnDefinition = "vector(1536)")
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = 1536)
    @Type(VectorType.class)
    private List<Double> embedding;

    @Column
    private String tags;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name="creationDate")
    private LocalDateTime creationDate;

    @Column(name="lastActivityDate")
    private LocalDateTime lastActivityDate;
    
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers;

    public Question() {

    }
    
    public Question(String tags, String body, LocalDateTime creationDate, LocalDateTime lastActivityDate) {
        this.tags = tags;
        this.body = body;
        this.creationDate = creationDate;
        this.lastActivityDate = lastActivityDate;
    }

    public Question(Long id, String tags, String body, LocalDateTime creationDate, LocalDateTime lastActivityDate) {
        this.id = id;
        this.tags = tags;
        this.body = body;
        this.creationDate = creationDate;
        this.lastActivityDate = lastActivityDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(LocalDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

	@Override
	public String toString() {
		return "Question [id=" + id + ", tags=" + tags + ", body=" + body + ", creationDate=" + creationDate
				+ ", lastActivityDate=" + lastActivityDate + ", answers=" + answers + "]";
	}

    public String toRagContext(){
        return String.format(
                "body:\n %s",
                body
        );
    }

	
}
