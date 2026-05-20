package it.nukleo.recaptelegrambot.telegram.service;


import it.nukleo.recaptelegrambot.llm.service.LlmService;
import it.nukleo.recaptelegrambot.telegram.web.TelegramApiClient;
import it.nukleo.recaptelegrambot.telegram.dto.request.TelegramSendMessageDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramChatDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramMessageDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramUpdateDto;
import it.nukleo.recaptelegrambot.telegram.persistence.entity.TelegramChatEntity;
import it.nukleo.recaptelegrambot.telegram.persistence.entity.TelegramMessageEntity;
import it.nukleo.recaptelegrambot.telegram.persistence.repository.TelegramMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private final TelegramMessageRepository telegramMessageRepository;
    private final TelegramApiClient telegramApiClient;
    private final LlmService llmService;

    public void handleUpdate(TelegramUpdateDto update) {
        if(update.getMessage() != null) {
            handleMessage(update.getMessage());
        }

        //gestire altri tipi di evento?
    }

    private void handleMessage(TelegramMessageDto message) {
        String chatType=message.getChat().getType();
        String text =  message.getText();
        Long chatId = message.getChat().getId();
        if (message.getFrom().getIsBot() ||(text == null || text.isBlank()) || (!"group".equals(chatType) && !"supergroup".equals(chatType))) {
            return;
        }

        //comandi
        if(text.startsWith("/recap") || text.startsWith("/Recap")) {
            LocalDateTime to = LocalDateTime.now();
            LocalDateTime from = to.minusHours(24);
            List<TelegramMessageEntity> messages = telegramMessageRepository.findMessagesForRecap(chatId, from, to);

            llmService.generateRecap(messages)
                    .thenAccept(recap -> sendMessage(chatId, recap))
                    .exceptionally(ex -> {
                        sendMessage(chatId, "Errore durante la generazione del recap");
                        System.out.printf("Errore: %s", ex.getMessage());
                        return null;
                    });

            return;
        }



        //se nessun comando: salvo messaggio
        TelegramMessageEntity savedMessage = saveMessage(message);
    }




    //chiama apiclient

    private void sendMessage(Long chatid, String text){
        TelegramSendMessageDto telegramSendMessageDto = new TelegramSendMessageDto();
        telegramSendMessageDto.setChatId(chatid);
        telegramSendMessageDto.setText(text);
        telegramApiClient.sendMessage(telegramSendMessageDto);
    }



    //mappa e salva entita

    private TelegramMessageEntity saveMessage(TelegramMessageDto dto) {
        TelegramMessageEntity savedMessage = new TelegramMessageEntity();
        savedMessage.setChatId(dto.getChat().getId());
        savedMessage.setText(dto.getText());
        savedMessage.setSentAt(
                Instant.ofEpochSecond(dto.getDate())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
        );
        savedMessage.setUserFirstName(dto.getFrom().getFirstName());
        return telegramMessageRepository.save(savedMessage);
    }

}
