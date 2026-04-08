package com.ufrn.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufrn.api.entities.Log;

public interface LogRepository extends JpaRepository<Log, Integer>{

}
