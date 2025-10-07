package com.ufrn.api.service;

import java.util.*;
import java.util.stream.Collectors;

import com.ufrn.dtos.*;
import org.apache.commons.text.similarity.LongestCommonSubsequence;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufrn.api.entities.Log;
import com.ufrn.api.entities.Question;
import com.ufrn.api.repository.LogRepository;
import com.ufrn.api.repository.QuestionRepository;

@Service
@Transactional
public class QuestionService {

	private static final float MATCH_THRESHOLD = 0.7f;
	private static final int MATCH_CNT = 3;

	@Autowired
	QuestionRepository questionRepository;

	@Autowired
	LogRepository logRepository;

	@Autowired
	EmbeddingClient embeddingClient;

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

    /*	Quando for popular o banco, lembrar de colocar a saída dessa função como campo embedding,
    como parâmetro dessa função passar o body */
	public List<Double> getTextEmbedding(String text)  {
        final int MAX_TEXT_LENGTH = 15000;

        System.out.println("embedding for... " + text.length());

        if (text.length() > MAX_TEXT_LENGTH) {
            text = text.substring(0, MAX_TEXT_LENGTH);
        }

        return embeddingClient.embed(text);
	}
//	Quando for popular o banco, lembrar de colocar a saída dessa função como campo embedding,
//	como parâmetro dessa função passar o body

	private String formatEmbeddingForQuerry(List<Double> embedding) {
		return  "[" +
				embedding.stream()
						.map(d -> String.format(Locale.US, "%.6f", d))
						.collect(Collectors.joining(",")) +
				"]";
	}

	public List<QuestionLinkDTO> getQuestionsBySemanticSearch(String message) {
		List<Double> messageEmbedding = getTextEmbedding(message);
		String formatedMessageEmbedding = formatEmbeddingForQuerry(messageEmbedding);
		return questionRepository.findQuestionBySimilarity(formatedMessageEmbedding, MATCH_THRESHOLD, MATCH_CNT);
	}
}
