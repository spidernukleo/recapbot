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

    private String recapBasePrompt;
    private String recapKeywordPrompt;

    @PostConstruct
    void init() {
        recapBasePrompt = loadPrompt("classpath:prompts/recapBasePrompt.txt");
        recapKeywordPrompt = loadPrompt("classpath:prompts/recapKeywordPrompt.txt");

    }

    public CompletableFuture<String> generateRecap(List<TelegramMessageEntity> messages, String keyword) {
        String prompt = buildPrompt(messages, keyword);
        return llmClient.generateText(prompt);
    }

    private String buildPrompt(List<TelegramMessageEntity> messages, String keyword) {
        String messagesText = messages.stream()
                .map(this::formatMessageForPrompt)
                .collect(Collectors.joining("\n"));

        String keywordPrompt = (keyword == null) ? "" : recapKeywordPrompt.replace("${KEYWORD}", keyword.trim());

        return recapBasePrompt
                .replace("${KEYWORD}", keywordPrompt)
                .replace("${CHAT_ID}", messages.getFirst().getChatId().toString().substring(4))
                .replace("${MESSAGES}", messagesText);
    }



    private String formatMessageForPrompt(TelegramMessageEntity message) {
        return "[MID_%d][%s] %s: %s".formatted(
                message.getMessageId(),
                message.getSentAt(),
                message.getUserFirstName(),
                message.getText()
        );
    }

    private String loadPrompt(String path) {
        try {
            return StreamUtils.copyToString(
                    resourceLoader.getResource(path).getInputStream(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new IllegalStateException("Errore durante la lettura del prompt file: " + path, e);
        }
    }
}