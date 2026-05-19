package it.nukleo.recaptelegrambot.llm;

import java.util.concurrent.CompletableFuture;

public interface LLmService {
    CompletableFuture<String> generateText(String prompt);
}
