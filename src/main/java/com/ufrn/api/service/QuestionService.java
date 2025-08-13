package com.ufrn.api.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.text.similarity.LongestCommonSubsequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufrn.api.entities.Log;
import com.ufrn.api.entities.Question;
import com.ufrn.api.repository.LogRepository;
import com.ufrn.api.repository.QuestionRepository;
import com.ufrn.dtos.AllResults;
import com.ufrn.dtos.ExceptionsEnum;
import com.ufrn.dtos.QuestionDTO;
import com.ufrn.dtos.QuestionResponseDTO;
import com.ufrn.dtos.ResponseDTO;
import com.ufrn.dtos.TopSimilarity;

@Service
@Transactional
public class QuestionService {

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	LogRepository logRepository;

	public List<QuestionDTO> getQuestions() {
		List<Question> questions = questionRepository.findAll();

		List<QuestionDTO> questionsDTO = questions.stream().map(QuestionDTO::new).collect(Collectors.toList());

		return questionsDTO;
	}

	public List<QuestionDTO> getQuestionsByAnnotation(String annotation) {
		List<Question> questions = questionRepository.findQuestionByAnnotation("%@" + annotation + "%");

		List<QuestionDTO> questionsDTO = questions.stream().map(QuestionDTO::new).collect(Collectors.toList());

		return questionsDTO;
	}

	public ResponseDTO getQuestionsByException(ExceptionsEnum exceptionEnum, String message) {
		LinkedList<Object[]> result = questionRepository.findQuestionByExceptionAlternative(exceptionEnum.getName());

		// stream-based approach to process the results more efficiently
		Map<String, List<QuestionResponseDTO>> questionsByAnnotation = result.stream()
				.map(r -> {
					String fullMessage = r[2].toString();
					if (fullMessage.toUpperCase().contains("CAUSED BY:") || fullMessage.toUpperCase().contains("EXCEPTION IN THREAD")) {
						LongestCommonSubsequence lcs = new LongestCommonSubsequence();
						String subsequence = String.valueOf(lcs.longestCommonSubsequence(message, fullMessage));
						return new QuestionResponseDTO(r[0].toString(), ((Long) r[1]).intValue(), subsequence.length());
					}
					return null;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.groupingBy(QuestionResponseDTO::getAnnotation));

		List<QuestionResponseDTO> allQuestions = questionsByAnnotation.values().stream()
				.flatMap(List::stream)
				.sorted(Comparator.comparing(QuestionResponseDTO::getSimilarityLength).reversed())
				.collect(Collectors.toList());

		// Handle the empty result case early to avoid unnecessary processing
		if (allQuestions.isEmpty()) {
			logRepository.save(new Log(Calendar.getInstance(), null, 0, null, exceptionEnum.getName(), message));
			return new ResponseDTO(Collections.emptyList(), Collections.emptyList());
		}

		// Process topSimilarity
		List<TopSimilarity> topSimilarity = new ArrayList<>();
		if (message != null && !message.isEmpty() && !message.equals("{}")) {
			Set<Integer> processedIds = new HashSet<>();
			topSimilarity = allQuestions.stream()
					.filter(q -> processedIds.add(q.getId()))
					.limit(10)
					.map(q -> {
						String annotationsList = questionsByAnnotation.get(q.getAnnotation()).stream()
								.map(QuestionResponseDTO::getAnnotation)
								.distinct()
								.collect(Collectors.joining(", "));
						return new TopSimilarity(annotationsList, "https://stackoverflow.com/questions/" + q.getId());
					})
					.collect(Collectors.toList());
		}

		// Process allResults and logging
		List<AllResults> allResults = questionsByAnnotation.entrySet().stream()
				.map(entry -> {
					String annotation = entry.getKey();
					List<String> idsList = entry.getValue().stream()
							.map(q -> "https://stackoverflow.com/questions/" + q.getId())
							.collect(Collectors.toList());
					logRepository.save(new Log(Calendar.getInstance(), String.join(", ", idsList), idsList.size(), annotation, exceptionEnum.getName(), message));
					return new AllResults(annotation, idsList.size(), idsList);
				})
				.sorted(Comparator.comparing(AllResults::getCount).reversed())
				.collect(Collectors.toList());

		return new ResponseDTO(topSimilarity, allResults);
	}
}
