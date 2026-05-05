package com.ufrn.api.repository;

import java.util.LinkedList;
import java.util.List;

import com.ufrn.dtos.QuestionLinkDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ufrn.api.entities.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer>{

	@Query(value = "SELECT q.* "
			+ "FROM question q "
			+ "JOIN question_annotation qa ON q.id = qa.question_id "
			+ "JOIN question_exception qe ON q.id = qe.question_id "
			+ "JOIN answer a ON q.id = a.question_id "
			+ "WHERE UPPER(qa.annotation) LIKE UPPER(:annotation) "
			+ "AND "
			+ "(a.isAccepted = true OR a.score > 0) " // Fix 1
			+ "AND "
			+ "UPPER(a.body) LIKE UPPER(:annotation) "
			+ "ORDER BY q.id", nativeQuery = true)
	public List<Question> findQuestionByAnnotation(@Param("annotation") String annotation);

	@Query(value = "SELECT DISTINCT(qa.annotation), (select group_concat(q1.id SEPARATOR ', ') as ids from question q1 join question_annotation q2 ON q1.id = q2.question_id JOIN question_exception q3 ON q1.id = q3.question_id  "
			+ "JOIN answer a1 ON q1.id = a1.question_id  "
			+ "where q2.annotation = qa.annotation and q3.exception = :exception AND (a1.isAccepted = true OR a1.score > 0)) as qnt, q.body " // Fix 2
			+ "FROM question q "
			+ "JOIN question_annotation qa ON q.id = qa.question_id "
			+ "JOIN question_exception qe ON q.id = qe.question_id "
			+ "JOIN answer a ON q.id = a.question_id "
			+ "WHERE qe.exception = :exception "
			+ "AND "
			+ "(a.isAccepted = true OR a.score > 0) " // Fix 3
			+ "ORDER BY LENGTH(qnt) desc, qa.annotation", nativeQuery = true)
	public List<Object[]> findQuestionByException(@Param("exception") String exception);

	@Query(value = "SELECT DISTINCT(qa.annotation), q.id, q.body "
			+ "FROM question q "
			+ "JOIN question_annotation qa ON q.id = qa.question_id "
			+ "JOIN question_exception qe ON q.id = qe.question_id "
			+ "JOIN answer a ON q.id = a.question_id "
			+ "WHERE qe.exception = :exception "
			+ "AND "
			+ "(a.isAccepted = true OR a.score > 0) " // Fix 4
			+ "ORDER BY qa.annotation", nativeQuery = true)
	public LinkedList<Object[]> findQuestionByExceptionAlternative(@Param("exception") String exception);

	@Query(value = """
    SELECT q.id AS question_id, 1 - (q.embedding <=> (:messageEmbedding)::vector) AS similarity
    FROM question q
    WHERE (1 - (q.embedding <=> (:messageEmbedding)::vector)) > :match_threshold
    ORDER BY similarity DESC
    LIMIT :match_cnt
""", nativeQuery = true)
	List<QuestionLinkDTO> findQuestionBySimilarity(
			@Param("messageEmbedding") String messageEmbedding,
			@Param("match_threshold") float matchThreshold,
			@Param("match_cnt") int matchCnt
	);

	@Query(value = """
    SELECT q.id AS question_id, 1 - (q.embedding <=> (:messageEmbedding)::vector) AS similarity
    FROM question q
    JOIN question_exception qe ON q.id = qe.question_id
    WHERE (1 - (q.embedding <=> (:messageEmbedding)::vector)) > :match_threshold
    AND qe.exception = :exception
    ORDER BY similarity DESC
    LIMIT :match_cnt
""", nativeQuery = true)
	List<QuestionLinkDTO> findQuestionByExceptionAndSimilarity(
			@Param("exception") String exception,
			@Param("messageEmbedding") String messageEmbedding,
			@Param("match_threshold") float matchThreshold,
			@Param("match_cnt") int matchCnt
	);

}
