package com.ufrn.api.controller;

import com.ufrn.api.service.PromptService;
import com.ufrn.api.service.QuestionService;
import com.ufrn.dtos.QuestionLinkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    @Autowired
    private PromptService promptService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<String> getRag(@RequestBody(required=true) String message) {
        String response = promptService.prompt(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    @CrossOrigin
    public ResponseEntity<String> getList(@RequestBody(required=true) String message) {
        List<QuestionLinkDTO> questions = questionService.getQuestionsBySemanticSearch(message);
        StringBuilder responseBuffer = new StringBuilder();
        for (QuestionLinkDTO question : questions) {
            responseBuffer.append(question.toString());
        }
        String response = responseBuffer.toString();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}