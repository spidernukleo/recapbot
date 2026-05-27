package it.nukleo.recaptelegrambot.llm.web;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Service
public class OpenAiLlmClient implements LlmClient {

    @Override
    public CompletableFuture<String> generateTextFromPrompt(String prompt) {
        return null;
    }

    @Override
    public CompletableFuture<String> transcribeAudio(Path path) {
        return null;
    }

}
