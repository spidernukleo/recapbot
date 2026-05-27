package it.nukleo.recaptelegrambot.llm.web;

import java.util.concurrent.CompletableFuture;

public class LocalLlmClient implements LlmClient {
    @Override
    public CompletableFuture<String> generateText(String prompt) {
        return null;
    }

    @Override
    public CompletableFuture<String> transcribeText(byte[] audioBytes) {
        return null;
    }
}
