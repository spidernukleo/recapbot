package it.nukleo.recaptelegrambot.telegram.controller;


import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramUpdateDto;
import it.nukleo.recaptelegrambot.telegram.service.TelegramWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TelegramWebhookController {

    private final TelegramWebhookService telegramWebhookService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> onUpdate(@RequestBody TelegramUpdateDto update) {
        try{
            telegramWebhookService.handleUpdate(update);
        } catch (Exception e){
            System.out.println("ERRORE "+e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
