package com.ufrn.api.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufrn.api.entities.LogWeb;
import com.ufrn.api.repository.LogWebRepository;
import com.ufrn.dtos.LogWebDTO;

@Service
@Transactional
public class LogWebService {
	
	@Autowired
	LogWebRepository logWebRepository;

	public void insertLogsWeb(LogWebDTO logWeb) {
		if (logWeb.getDados().length == 0) {
			logWebRepository.save(new LogWeb(null, logWeb.getIdUsuario(), Calendar.getInstance(), -1, logWeb.getEstrategia(), null, logWeb.getStacktrace(), logWeb.getException()));
		}
		
		for (int i = 0; i < logWeb.getDados().length; i++) {			
			logWebRepository.save(new LogWeb(null, logWeb.getIdUsuario(), Calendar.getInstance(), i, logWeb.getEstrategia(), logWeb.getDados()[i], logWeb.getStacktrace(), logWeb.getException()));
		}
	}
	

	
}
