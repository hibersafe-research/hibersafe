package com.ufrn.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufrn.api.service.LogWebService;
import com.ufrn.dtos.LogWebDTO;

@RestController
@RequestMapping("/api/log")
public class LogController {
	
	@Autowired
	private LogWebService logWebService;
	
	@PostMapping("/")
	@CrossOrigin
	public ResponseEntity<Boolean> insertLogsWeb(@RequestBody(required=true) LogWebDTO logWeb) {
		
		logWebService.insertLogsWeb(logWeb);
		
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

}
