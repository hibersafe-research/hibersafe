package com.ufrn.api.controller;

import com.ufrn.api.service.PromptService;
import com.ufrn.api.service.QuestionService;
import com.ufrn.dtos.ExceptionsEnum;
import com.ufrn.dtos.QuestionLinkDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    @Autowired
    private PromptService promptService;

    @Autowired
    private QuestionService questionService;

//    todo: fazer exception ser um enum

    @PostMapping({"/prompt", "/prompt/{exception}"})
    @CrossOrigin
    public ResponseEntity<String> getRag(
            @PathVariable Optional<ExceptionsEnum> exception,
            @RequestBody(required=true) String message,
            @RequestParam Optional<Integer> link_count,
            @RequestParam Optional<Float> min_similarity,
            @RequestParam(required = false, defaultValue = "true") boolean rag,
            @RequestParam(required = false, defaultValue = "false") boolean just_so,
            @RequestParam(required = false) String limitDate,
            @RequestParam(required = false, defaultValue = "false") boolean clean_stack_trace
    ) {
        String stack_trace;
        String response;
        if(clean_stack_trace){
            stack_trace = questionService.clean_stack_trace(message);
        }else {
            stack_trace = message;
        }
        if(just_so){
            response = promptService.prompt_just_so(stack_trace, limitDate);
        } else if(rag){
            response = promptService.prompt_with_context(exception, stack_trace, link_count, min_similarity);
        }else {
            response = promptService.simple_prompt(stack_trace);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping({"/list", "/list/{exception}"})
    @CrossOrigin
    public ResponseEntity<String> getList(
            @PathVariable Optional<ExceptionsEnum> exception,
            @RequestBody(required=true) String message,
            @RequestParam Optional<Integer> link_count,
            @RequestParam Optional<Float> min_similarity
    ) {
        List<QuestionLinkDTO> questions = questionService.getQuestionsBySemanticSearch(message, exception, link_count, min_similarity);
        StringBuilder responseBuffer = new StringBuilder();
        for (QuestionLinkDTO question : questions) {
            responseBuffer.append(question.toString());
            responseBuffer.append("\n");
        }
        String response = responseBuffer.toString();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}