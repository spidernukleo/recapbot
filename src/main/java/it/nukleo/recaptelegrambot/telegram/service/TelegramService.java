package it.nukleo.recaptelegrambot.telegram.service;


import it.nukleo.recaptelegrambot.llm.service.LlmService;
import it.nukleo.recaptelegrambot.telegram.dto.request.TelegramSendReactionDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramReactionEmojiDto;
import it.nukleo.recaptelegrambot.telegram.web.TelegramApiClient;
import it.nukleo.recaptelegrambot.telegram.dto.request.TelegramSendMessageDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramMessageDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramUpdateDto;
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
    }

    private void handleMessage(TelegramMessageDto message) {
        String chatType = message.getChat().getType();
        if (!"group".equals(chatType) && !"supergroup".equals(chatType)) {
            return;
        }

        if (Boolean.TRUE.equals(message.getFrom().getIsBot())) {
            return;
        }

        if (message.getVoice() != null) {
            System.out.println("Voice arrivato");
            return;
        }

        String text =message.getText();

        if (text == null || text.isBlank()) {
            return;
        }

        if (text.trim().toLowerCase().startsWith("/recap")) {
            handleRecap(message);
            return;
        }

        saveMessage(message);
    }


    private void handleRecap(TelegramMessageDto message) {
        Long chatId = message.getChat().getId();
        String text = message.getText();
        Long messageId = message.getMessageId();
        String[] parts = text.trim().split("\\s+");

        if(parts.length > 2) {
            telegramApiClient.sendReaction(chatId, messageId, "👎");
            return;
        }

        telegramApiClient.sendReaction(chatId, messageId, "👀");

//            List<TelegramMessageEntity> messages = telegramMessageRepository.findMessagesForRecap(chatId, from, to);
//
//            if (messages.isEmpty()) {
        //  telegramApiClient.sendMessage(chatId, "Non ho trovato messaggi nel periodo richiesto.");
//                return;
//            }
//
//            System.out.printf(
//                    "DEBUG - recap richiesto. durata=%s, keyword=%s%n",
//                    command.duration(),
//                    command.keyword()
//            );
//
//            llmService.generateRecap(messages)
//                    .thenAccept(recap -> sendMessage(chatId, recap))
//                    .exceptionally(ex -> {
//                        sendMessage(chatId, "Errore durante la generazione del recap");
//                        System.out.printf("Errore: %s%n", ex.getMessage());
//                        return null;
//                    });
//
//        } catch (IllegalArgumentException ex) {
//            sendMessage(message.getChat().getId(), ex.getMessage());
//        }

    }

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
        savedMessage.setMessageId(dto.getMessageId());
        return telegramMessageRepository.save(savedMessage);
    }

}
