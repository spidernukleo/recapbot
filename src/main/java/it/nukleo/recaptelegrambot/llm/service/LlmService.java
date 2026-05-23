package it.nukleo.recaptelegrambot.llm.service;


import it.nukleo.recaptelegrambot.llm.web.LlmClient;
import it.nukleo.recaptelegrambot.telegram.persistence.entity.TelegramMessageEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LlmService {

    @Qualifier("geminiLlmClient")
    private final LlmClient llmClient;

    private final ResourceLoader resourceLoader;

    private String recapTemplate;

    @PostConstruct
    void init() {
        recapTemplate = loadPrompt();
    }

    public CompletableFuture<String> generateRecap(List<TelegramMessageEntity> messages, String keyword) {
        String prompt = buildPrompt(messages, keyword);
        return llmClient.generateText(prompt);
    }

    private String buildPrompt(List<TelegramMessageEntity> messages, String keyword) {
        String messagesText = messages.stream()
                .map(this::formatMessageForPrompt)
                .collect(Collectors.joining("\n"));

        String focusRules;

        if (keyword == null || keyword.isBlank()) {
            focusRules = """
                Obiettivo:
                estrarre solo fatti, eventi, decisioni, aggiornamenti o richieste rilevanti emersi nei messaggi.

                Criteri di selezione:
                - Dai priorità massima alle informazioni universitarie e organizzative.
                - Includi richieste utili solo se contengono un'informazione concreta o un bisogno rilevante per gli studenti.
                - Escludi messaggi ripetitivi se non aggiungono nuove informazioni.
                - Escludi conversazioni generiche che non producono un fatto utile o recuperabile.
                - Se non emergono informazioni davvero rilevanti, rispondi esattamente con:
                  - Nessuna informazione rilevante emersa nel periodo considerato.
                """;
        } else {
            focusRules = """
                In questo caso devi concentrarti solo sulle informazioni collegate alla seguente parola chiave o argomento:

                PAROLA CHIAVE: "%s"

                Obiettivo:
                estrarre solo fatti, eventi, decisioni, aggiornamenti o richieste rilevanti che riguardano esplicitamente la parola chiave indicata oppure che sono chiaramente collegati ad essa nel contesto dei messaggi.

                Criteri di selezione:
                - Includi solo contenuti collegati alla parola chiave indicata.
                - Considera rilevanti anche messaggi che non ripetono letteralmente la parola chiave ma che si riferiscono chiaramente allo stesso argomento nel contesto della conversazione.
                - Escludi tutto ciò che non è collegato alla parola chiave, anche se potrebbe essere rilevante in generale per il gruppo.
                - Dai priorità massima alle informazioni universitarie e organizzative collegate alla parola chiave.
                - Escludi messaggi ripetitivi se non aggiungono nuove informazioni.
                - Escludi chiacchiere, battute, saluti e rumore conversazionale.
                - Se non emergono informazioni rilevanti collegate alla parola chiave, rispondi esattamente con:
                  - Nessuna informazione rilevante emersa sulla parola chiave indicata nel periodo considerato.
                """.formatted(keyword);
        }

        return recapTemplate
                .replace("${FOCUS_RULES}", focusRules)
                .replace("${CHAT_ID}", messages.getFirst().getChatId().toString().substring(4))
                .replace("${MESSAGES_TEXT}", messagesText);
    }

    private String formatMessageForPrompt(TelegramMessageEntity message) {
        return "[MID_%d][%s] %s: %s".formatted(
                message.getMessageId(),
                message.getSentAt(),
                message.getUserFirstName(),
                message.getText()
        );
    }

    private String loadPrompt() {
        Resource resource = resourceLoader.getResource("classpath:prompts/recap.txt");

        try {
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Errore durante la lettura del prompt file");
        }
    }
}