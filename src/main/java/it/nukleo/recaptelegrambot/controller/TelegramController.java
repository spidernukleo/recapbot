package it.nukleo.recaptelegrambot.controller;


import it.nukleo.recaptelegrambot.service.TelegramService;
import it.nukleo.recaptelegrambot.dto.response.TelegramUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramService telegramWebhookService;

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
