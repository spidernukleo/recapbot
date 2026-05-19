package it.nukleo.recaptelegrambot.telegram.service;


import it.nukleo.recaptelegrambot.llm.GeminiApiClient;
import it.nukleo.recaptelegrambot.llm.LLmService;
import it.nukleo.recaptelegrambot.telegram.TelegramApiClient;
import it.nukleo.recaptelegrambot.telegram.dto.request.TelegramSendMessageDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramChatDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramMessageDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramUpdateDto;
import it.nukleo.recaptelegrambot.telegram.persistence.entity.TelegramChatEntity;
import it.nukleo.recaptelegrambot.telegram.persistence.entity.TelegramMessageEntity;
import it.nukleo.recaptelegrambot.telegram.persistence.repository.TelegramChatRepository;
import it.nukleo.recaptelegrambot.telegram.persistence.repository.TelegramMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private final TelegramChatRepository telegramChatRepository;
    private final TelegramMessageRepository telegramMessageRepository;
    private final TelegramApiClient telegramApiClient;
    private final LLmService llmService;

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
        if(text.equals("/start")){
            this.sendMessage("ciao **test**", chatId);
            return;
        }
        if(text.startsWith("/gemini")){
            String prompt = text.replaceFirst("^/gemini\\s*", "");
            prompt = prompt + "\nRispondi solo con testo semplice, senza formattazione.\n" +
                    "Non usare markdown, html, grassetto, corsivo, liste, blocchi di codice o tabelle.\n" +
                    "Evita simboli speciali inutili.\n" +
                    "Se ti serve fare un elenco, usa frasi normali separate da a capo.";

            llmService.generateText(prompt)
                    .thenAccept(text -> sendMessage(text, chatId))
                    .exceptionally(ex-> {
                        sendMessage("Errore nella generazione della risp", chatId);
                        System.out.println("errore" +ex.getMessage());
                        return null;
                    });

            return;
        }

        //se nessun comando: salvo messaggio
        TelegramMessageEntity savedMessage = saveMessage(message);
    }


    //chiama apiclient

    private void sendMessage(String text, Long chatid){
        TelegramSendMessageDto telegramSendMessageDto = new TelegramSendMessageDto();
        telegramSendMessageDto.setChatId(chatid);
        telegramSendMessageDto.setText(text);
        telegramApiClient.sendMessage(telegramSendMessageDto);
    }

    //mappa e salva entita



    private TelegramMessageEntity saveMessage(TelegramMessageDto dto) {
        TelegramMessageEntity savedMessage = new TelegramMessageEntity();
        savedMessage.setChatId(dto.getChat().getId());
        savedMessage.setMessageId(dto.getMessageId());
        savedMessage.setText(dto.getText());
        savedMessage.setSentAt(
                Instant.ofEpochSecond(dto.getDate())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
        );
        savedMessage.setUserFirstName(dto.getFrom().getFirstName());
        savedMessage.setUserId(dto.getFrom().getId());
        if (dto.getReplyToMessage() != null) {
            savedMessage.setReplyToMessageId(dto.getReplyToMessage().getMessageId());
        }
        return telegramMessageRepository.save(savedMessage);
    }

    private TelegramChatEntity getOrCreateChat(TelegramChatDto chatDto) {
        return telegramChatRepository.findByChatId(chatDto.getId())
                .orElseGet(() -> {
                    TelegramChatEntity chat = new TelegramChatEntity();
                    chat.setChatId(chatDto.getId());
                    chat.setType(chatDto.getType());
                    return telegramChatRepository.save(chat);
                });
    }

}
