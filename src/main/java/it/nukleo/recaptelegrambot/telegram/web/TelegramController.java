package it.nukleo.recaptelegrambot.telegram.web;


import it.nukleo.recaptelegrambot.telegram.service.TelegramService;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramService telegramService;

    @PostMapping("/webhook")
    public ResponseEntity<Void> onUpdate(@RequestBody TelegramUpdateDto update) {
        try{
            telegramService.handleUpdate(update);
        } catch (Exception e){
            System.out.println("ERRORE "+e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
