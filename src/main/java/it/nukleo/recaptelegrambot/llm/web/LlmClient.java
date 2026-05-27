package it.nukleo.recaptelegrambot.llm.web;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface LlmClient {
    CompletableFuture<String> generateTextFromPrompt(String prompt);

    CompletableFuture<String> transcribeAudio(Path path) throws Exception;

}
