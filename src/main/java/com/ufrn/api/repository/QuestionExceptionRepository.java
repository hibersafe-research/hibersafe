package com.ufrn.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufrn.api.entities.QuestionException;

public interface QuestionExceptionRepository extends JpaRepository<QuestionException, Integer> {

}
