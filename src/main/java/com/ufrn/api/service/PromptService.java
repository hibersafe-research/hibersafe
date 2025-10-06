package com.ufrn.api.service;

import com.ufrn.api.entities.Question;
import com.ufrn.dtos.QuestionLinkDTO;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PromptService {
    private QuestionService questionService;
    private ChatClient aiClient;

    public PromptService(QuestionService questionService, ChatClient aiClient) {
        this.questionService = questionService;
        this.aiClient = aiClient;
    }

    public String prompt(String message) {
        List<QuestionLinkDTO> relevantLinks = questionService.getQuestionsBySemanticSearch(message);

        String context = relevantLinks.stream()
          .map(QuestionLinkDTO::toRagContext)
          .collect(Collectors.joining("\n\n"));


        PromptTemplate template = new PromptTemplate(
        """
        Você é o assietente de programação Hibersafe, um assistente de IA especialista execeções geradas por anotações do Hibernate. Sua principal missão é ajudar a entender o que causou o lançamento da excessão e como fazer o programa funcionar corretamente.
               \s
        Siga estas regras RIGOROSAMENTE:
               \s
        1.  *ÊNFASE NO CONTEXTO:* Responda utilizando principalemnte a informação contida nos nas páginas do Stack Overflow fornecidas abaixo. Caso tenha outras informações pode fazer uso delas.
        2.  *CITE QUANDO POSSÍVEL:* Caso se baseie em uma informação, cite um local onde pode achar mais informações sobre.
        3.  *SEJA DIRETO E SINTETIZE:* Forneça uma resposta direta e clara para a pergunta do usuário. Sintetize a informação das perguntas e respostas relevantes encontradas no Stack Overflow. Não se limite a listar ou repetir os trechos fornecidos.
        4.  *TOM PROFISSIONAL:* Mantenha sempre um tom profissional, técnico e formal. Sua linguagem deve ser descritiva e precisa.
        5.  *QUANDO FALTAR INFORMAÇÂO, DIGA:* Se precisar de mais informação sobre o projeto para dar uma boa resposta diga de forma clara, e informe o que exatamente quer, não tente adivinhar.
        6.  *QUANDO NÃO SOUBER, DIGA:* Se a informação necessária para responder à pergunta não estiver no contexto fornecido, declare explicitamente: "A informação solicitada não foi encontrada no Stack Overflow." Não tente adivinhar.

        Links:
        {context}
   \s""");

        Map<String, Object> variables = Map.of(
          "context", context
        );

        Prompt chatPrompt = template.create(variables);

        return aiClient.call(chatPrompt).getResult().getOutput().getContent();
    }
}
