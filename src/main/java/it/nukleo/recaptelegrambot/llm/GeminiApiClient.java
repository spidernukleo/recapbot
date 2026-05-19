package it.nukleo.recaptelegrambot.llm;

import it.nukleo.recaptelegrambot.config.GeminiApiProperties;
import it.nukleo.recaptelegrambot.telegram.dto.request.GeminiRequestDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.GeminiResponseDto;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Service
public class GeminiApiClient {
    private final RestClient geminiRestClient;

    public GeminiApiClient(GeminiApiProperties properties) {
        this.geminiRestClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl() + "/" + properties.getModel())
                .defaultHeader("X-goog-api-key", properties.getKey())
                .build();
    }


    @Async("geminiExecutor")
    public CompletableFuture<String> generateContentAsync(String prompt){
        GeminiResponseDto response = geminiRestClient.post()
                .uri(":generateContent")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new GeminiRequestDto(prompt))
                .retrieve()
                .body(GeminiResponseDto.class);

        String text = response != null ? response.extractText() : null;
        return CompletableFuture.completedFuture(text);
    }
}
