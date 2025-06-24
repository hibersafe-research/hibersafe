package com.ufrn.api.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufrn.api.service.StackOverflowService;
import com.ufrn.dtos.PageDTO;

@RestController
@RequestMapping("/api/stackoverflow")
public class StackOverflowController {
	
	@Autowired
	StackOverflowService stackOverflowUtil;
	
	@GetMapping("/miner")
	public ResponseEntity<?> getQuestionsAndAnswers() throws IOException, InterruptedException{
        int pageNumber = 1;

        PageDTO page = stackOverflowUtil.sendHttpRequest(pageNumber);

        while (page.hasMore()) {
            // process data
            stackOverflowUtil.process(page);

            System.out.println("Processed page " + pageNumber);

            pageNumber++;
            page = stackOverflowUtil.sendHttpRequest(pageNumber);
        }
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
