package com.ufrn.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufrn.api.entities.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

}
