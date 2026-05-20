package it.nukleo.recaptelegrambot.llm.web;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OpenAiLlmClient implements LlmClient {

    @Override
    public CompletableFuture<String> generateText(String prompt) {
        return null;
    }

}
