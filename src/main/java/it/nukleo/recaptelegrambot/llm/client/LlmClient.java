package it.nukleo.recaptelegrambot.llm.client;

import java.util.concurrent.CompletableFuture;

public interface LlmClient {
    CompletableFuture<String> generateText(String prompt);
}
