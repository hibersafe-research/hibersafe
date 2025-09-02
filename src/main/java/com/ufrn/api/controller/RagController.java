package com.ufrn.api.controller;

import com.ufrn.api.service.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    @Autowired
    private PromptService promptService;

    @GetMapping("")
    @CrossOrigin
    public ResponseEntity<String> getRag(@RequestBody(required=true) String message) {
        String response = promptService.prompt(message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}