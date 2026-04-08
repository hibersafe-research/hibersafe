package com.ufrn.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufrn.api.entities.QuestionAnnotation;

public interface QuestionAnnotationRepository extends JpaRepository<QuestionAnnotation, Integer> {

}
