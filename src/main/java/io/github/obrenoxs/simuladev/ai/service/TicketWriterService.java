package io.github.obrenoxs.simuladev.ai.service;

import io.github.obrenoxs.simuladev.ai.client.*;
import io.github.obrenoxs.simuladev.ai.exception.AiResponseException;
import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import io.github.obrenoxs.simuladev.engine.result.EngineResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketWriterService {

    private final GeminiClient geminiClient;

    public TicketWriterService(GeminiClient geminiClient) {
        this.geminiClient = geminiClient;
    }

    public String generateTicket(EngineResult result, CompanyType companyType) {

        String systemText = """
            Você é um gerente de tecnologia numa empresa fictícia chamada %s.
            Seu trabalho é atribuir tarefas de desenvolvimento para um desenvolvedor júnior \
            da sua equipe, como parte da rotina normal de trabalho. Escreva de forma \
            profissional, mas natural e direta — como uma mensagem real que um gerente \
            mandaria no dia a dia, sem ser excessivamente formal nem robótico. Não use \
            saudações longas nem se despeça formalmente; vá direto ao ponto, como quem \
            já conversa com a pessoa todo dia.
            """.formatted(companyType.getDomainName());

        String userText = """
        Preciso que você implemente uma funcionalidade relacionada a: %s.
        Tipo de tarefa: %s
        Dificuldade estimada: %s
        Escreva o pedido como se fosse parte de uma conversa real, dando um contexto \
        de negócio plausível (por que isso é necessário agora), sem entrar em detalhes \
        técnicos de implementação — isso fica por conta do desenvolvedor.
        """.formatted(
                result.concept().getConceptName(),
                result.type(),
                result.difficulty()
        );

        Part systemTextPart = new Part(systemText);
        Part userTextPart = new Part(userText);

        List<Part> systemParts = new ArrayList<>();
        systemParts.add(systemTextPart);

        List<Part> userParts = new ArrayList<>();
        userParts.add(userTextPart);

        SystemInstruction systemInstruction = new SystemInstruction(systemParts);

        List<Content> contents = new ArrayList<>();
        Content content = new Content("user", userParts);
        contents.add(content);


        GeminiRequest geminiRequest = new GeminiRequest(systemInstruction, contents);

        GeminiResponse response = geminiClient.generateContent(geminiRequest);
        List<Candidate> candidates = response.candidates();

        if (candidates.isEmpty()) {
            throw new AiResponseException("A IA não retornou nenhum conteúdo");
        }

        Candidate firstCandidate = candidates.get(0);
        Part firstPart = firstCandidate.content().parts().get(0);
        String text = firstPart.text();

        return text;
    }
}
