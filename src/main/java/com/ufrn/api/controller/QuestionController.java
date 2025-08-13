package com.ufrn.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ufrn.api.service.QuestionService;
import com.ufrn.dtos.AnnotationsEnum;
import com.ufrn.dtos.ExceptionsEnum;
import com.ufrn.dtos.QuestionDTO;
import com.ufrn.dtos.ResponseDTO;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	@GetMapping("/all")
	public ResponseEntity<List<QuestionDTO>> getQuestions() {
		
		List<QuestionDTO> questions = questionService.getQuestions();
		
		return new ResponseEntity<List<QuestionDTO>>(questions, HttpStatus.OK);
	}
	
	@GetMapping("/annotation/{annotation}")
	public ResponseEntity<List<QuestionDTO>> getQuestionsByAnnotation(@PathVariable AnnotationsEnum annotation) {
		
		List<QuestionDTO> questions = questionService.getQuestionsByAnnotation(annotation.toString());
		
		return new ResponseEntity<List<QuestionDTO>>(questions, HttpStatus.OK);
	}
	
	@PostMapping("/exceptionEnum/{exceptionEnum}")
	@CrossOrigin
	public ResponseEntity<ResponseDTO> getQuestionsByException(@PathVariable ExceptionsEnum exceptionEnum, @RequestBody(required=false) String message) {
		
		ResponseDTO questions = questionService.getQuestionsByException(exceptionEnum, message);
		
		return new ResponseEntity<ResponseDTO>(questions, HttpStatus.OK);
	}

}
