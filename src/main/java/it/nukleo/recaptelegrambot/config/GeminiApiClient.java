package it.nukleo.recaptelegrambot.config;

import it.nukleo.recaptelegrambot.dto.request.GeminiRequestDto;
import it.nukleo.recaptelegrambot.dto.response.GeminiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class GeminiApiClient {

    private final RestClient geminiRestClient;

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
