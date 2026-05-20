package it.nukleo.recaptelegrambot.llm.web;

import java.util.concurrent.CompletableFuture;

public interface LlmClient {
    CompletableFuture<String> generateText(String prompt);
}
