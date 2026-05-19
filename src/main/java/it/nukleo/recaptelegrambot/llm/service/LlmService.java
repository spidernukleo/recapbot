package it.nukleo.recaptelegrambot.llm.service;


import it.nukleo.recaptelegrambot.llm.client.LlmClient;
import it.nukleo.recaptelegrambot.telegram.persistence.entity.TelegramMessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LlmService {
    @Qualifier("geminiLlmClient") //modifica qua col client llm che vuoi usare
    private final LlmClient llmClient;


    public CompletableFuture<String> generateRecap(List<TelegramMessageEntity> messages) {
        String prompt = this.buildPrompt(messages);
        return llmClient.generateText(prompt);
    }

    private String formatMessageForPrompt(TelegramMessageEntity message) {
        return "[%s] %s: %s".formatted(
                message.getSentAt(),
                message.getUserFirstName(),
                message.getText()
        );
    }

    private String buildPrompt(List<TelegramMessageEntity> messages) {
        String messagesText = messages.stream()
                .map(this::formatMessageForPrompt)
                .collect(Collectors.joining("\n"));


        return """
            Devi analizzare messaggi di una chat Telegram e produrre un recap per eventi distinti.

            Obiettivo:
            estrarre solo i fatti, eventi o discussioni rilevanti avvenuti nel periodo considerato.

            Regole obbligatorie di output:
            - Rispondi solo in italiano.
            - Rispondi solo in testo semplice.
            - Ogni riga deve iniziare con "- ".
            - Ogni riga deve avere obbligatoriamente questo formato:
              - [nome oppure nomi coinvolti] [cosa è successo]
            - Ogni bullet deve descrivere un solo evento, fatto o argomento rilevante.
            - Non unire nello stesso bullet eventi diversi, anche se coinvolgono le stesse persone.
            - Non raggruppare un'intera conversazione in un unico bullet se contiene temi diversi.
            - Se ci sono più persone coinvolte nello stesso singolo evento, scrivile.
            - Se i partecipanti sono molti, puoi scrivere "X, Y e altri" oppure "più partecipanti (X, Y, altri)".
            - Mantieni ogni bullet breve, concreto e informativo.
            - Se necessario, puoi aggiungere un breve dettaglio rilevante tra parentesi alla fine del bullet.
            - Le parentesi devono contenere solo informazioni brevi e utili, per esempio una data, un risultato, un luogo o un chiarimento.
            - Non usare parentesi per aggiungere commenti, interpretazioni o dettagli superflui.
            - Non inventare nulla.
            - Non dedurre sentimenti, relazioni o intenzioni se non sono espliciti nei messaggi.
            - Non usare frasi generiche come "hanno parlato di varie cose", "hanno discusso di diversi temi".
            - Se ci sono 4 eventi distinti, devi produrre 4 bullet distinti.
            - Non scrivere introduzioni, conclusioni o testo fuori dai bullet.

            Criteri di separazione:
            - Se cambia argomento, crea un nuovo bullet.
            - Se cambia il fatto raccontato, crea un nuovo bullet.
            - Se una persona racconta due cose diverse, crea due bullet distinti.
            - Unisci solo messaggi consecutivi che parlano chiaramente dello stesso identico fatto.

            Esempi corretti:
            - Mario aveva detto che sarebbe arrivato in ritardo alla cena (circa 20 minuti).
            - Mario e Rossi avevano deciso di spostare la riunione a venerdì.
            - più partecipanti (Mario, Rossi, Luca, altri) avevano discusso dei prezzi troppo alti del locale.
            - Luca aveva raccontato di aver superato un esame importante (30 e lode).
            - Anna aveva chiesto consigli per comprare un portatile nuovo.
            - Rossi aveva condiviso l'orario del treno per Roma (partenza alle 08:45).
           
            
            Esempi sbagliati:
            - Mario e Rossi: avevano parlato della cena, della riunione, dei treni e di altre cose.
            - Mario aveva fatto varie cose durante la giornata.
            - Alcuni partecipanti avevano discusso di diversi argomenti.
           
            Messaggi da analizzare:
            
            """
                + messagesText;
    }
}
