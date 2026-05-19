package it.nukleo.recaptelegrambot.llm.service;

import it.nukleo.recaptelegrambot.llm.client.GeminiApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiApiService {

    private final GeminiApiClient geminiApiClient;
}
