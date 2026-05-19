package it.nukleo.recaptelegrambot.llm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class GeminiLlmService implements LLmService {

    private final GeminiApiClient geminiApiClient;

    @Override
    public CompletableFuture<String> generateText(String prompt) {
        return geminiApiClient.generateContentAsync(prompt);
    }
}
