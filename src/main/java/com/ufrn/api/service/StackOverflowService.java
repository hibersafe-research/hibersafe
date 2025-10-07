package com.ufrn.api.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufrn.api.entities.Question;
import com.ufrn.api.entities.QuestionAnnotation;
import com.ufrn.api.entities.QuestionException;
import com.ufrn.api.repository.AnswerRepository;
import com.ufrn.api.repository.QuestionAnnotationRepository;
import com.ufrn.api.repository.QuestionExceptionRepository;
import com.ufrn.api.repository.QuestionRepository;
import com.ufrn.dtos.PageDTO;
import com.ufrn.dtos.QuestionDTO;
import com.ufrn.utils.Annotations;
import com.ufrn.utils.Exceptions;

@Service
public class StackOverflowService {

    @Autowired
    QuestionRepository questionRepository;
    
    @Autowired
    AnswerRepository answerRepository;
    
    @Autowired
    QuestionAnnotationRepository questionAnnotationRepository;
    
    @Autowired
    QuestionExceptionRepository questionExceptionRepository;

    @Autowired
    QuestionService questionService;

    public PageDTO sendHttpRequest(int page) throws IOException, InterruptedException {
        // 2010-03-31
        String fromDate = "fromdate=1269993600&";
        // 2025-05-31
        String toDate = "todate=1748649600&";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("https://api.stackexchange.com/2.3/questions?key=vtviiqbbd4qmJYPlsfYCKg((&" + fromDate
                        + toDate + "page=" + page + "&" + "pagesize=100&" + "order=asc&"
                        + "sort=creation&" + "tagged=hibernate&" + "site=stackoverflow"
                        + "&filter=!BLgprJqGKEK0a17JpVQydcQ22st_Xe"
                    //  + "&filter=!b*Ar.5VD4mpdg2L*VkuIiXMYmkK*r5"
                        ))
                .build();

        HttpResponse<byte[]> response = client.send(request, BodyHandlers.ofByteArray());

        String jsonString = new String(new ByteArrayInputStream(response.body()).readAllBytes(), StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        JsonNode jsonNode = mapper.readTree(jsonString);

        return mapper.readValue(jsonNode.toString(), PageDTO.class);
    }

    public void process(PageDTO page) {
        // Iterate through all pages
        for (QuestionDTO item : page.getItems()) {

            // Check question body for annotations
            Pattern patternAnnotation = Pattern.compile(Annotations.HIBERNATE_ANNOTATIONS + "|" + Annotations.JPA_ANNOTATIONS);

            Pattern patternException = Pattern.compile(Exceptions.HIBERNATE_EXCEPTIONS);
            
            Matcher matcher1 = patternAnnotation.matcher(item.getBody());
            Matcher matcher2 = patternException.matcher(item.getBody());
            
            List<String> annotations = new ArrayList<>();
            List<String> exceptions = new ArrayList<>();

            // If there's an annotation and an exception on the question
            if (matcher1.find() && matcher2.find()) {
            	matcher1.reset();
            	matcher2.reset();
            	
            	// Convert questionDTO to Question entity
                Question question = item.toQuestion();
                question.setEmbedding(questionService.getTextEmbedding(question.getBody()));

                // Save Question entity
                questionRepository.save(question);
                
                // If QuestionDTO has answers, convert them to entities and save them
                if (item.getAnswers() != null) {
                    item.getAnswers().stream().map(answer -> answer.toAnswer(question)).forEach(answerRepository::save);
                }
            	
            	while(matcher1.find()){
            		if (!annotations.contains(matcher1.group())){
            			annotations.add(matcher1.group());
            		}
            	}
            	
            	while(matcher2.find()){
            		if (!exceptions.contains(matcher2.group())){
            			exceptions.add(matcher2.group());
            		}
            	}
            	
            	for (String annotation : annotations) {
            		questionAnnotationRepository.save(new QuestionAnnotation(question, annotation));
            	}
            	
            	for (String exception : exceptions) {
            		questionExceptionRepository.save(new QuestionException(question, exception));
                    
        		}
            }
            
        }
    }
}
