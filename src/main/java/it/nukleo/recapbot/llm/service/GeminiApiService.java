package it.nukleo.recapbot.llm.service;

import it.nukleo.recapbot.llm.client.GeminiApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeminiApiService {

    private final GeminiApiClient geminiApiClient;
}
