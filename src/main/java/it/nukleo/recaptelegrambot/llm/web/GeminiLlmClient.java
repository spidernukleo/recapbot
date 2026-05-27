package it.nukleo.recaptelegrambot.llm.web;

import it.nukleo.recaptelegrambot.config.GeminiApiProperties;
import it.nukleo.recaptelegrambot.llm.dto.request.GeminiRequestDto;
import it.nukleo.recaptelegrambot.llm.dto.response.GeminiResponseDto;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Service("geminiLlmClient")
public class GeminiLlmClient implements LlmClient {
    private final RestClient geminiRestClient;

    public GeminiLlmClient(GeminiApiProperties properties) {
        this.geminiRestClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl() + "/" + properties.getModel())
                .defaultHeader("X-goog-api-key", properties.getKey())
                .build();
    }

    @Override
    @Async("textExecutor")
    public CompletableFuture<String> generateTextFromPrompt(String prompt) {
        GeminiResponseDto response = geminiRestClient.post()
                .uri(":generateContent")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GeminiRequestDto(prompt))
                .retrieve()
                .body(GeminiResponseDto.class);

        String text = response != null ? response.extractText() : null;
        return CompletableFuture.completedFuture(text);
    }

    @Override
    public CompletableFuture<String> transcribeAudio(Path path){
        return null;
    }
}
