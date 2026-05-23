package it.nukleo.recaptelegrambot.telegram.service;


import it.nukleo.recaptelegrambot.llm.service.LlmService;
import it.nukleo.recaptelegrambot.telegram.web.TelegramApiClient;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramMessageDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramUpdateDto;
import it.nukleo.recaptelegrambot.telegram.persistence.entity.TelegramMessageEntity;
import it.nukleo.recaptelegrambot.telegram.persistence.repository.TelegramMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Long senderId =  message.getFrom().getId();

        String[] parts = text.trim().split("\\s+");

        if (parts.length > 2) {
            telegramApiClient.sendReaction(chatId, messageId, "👎");
            return;
        }

        telegramApiClient.sendReaction(chatId, messageId, "👀");

        Duration duration = Duration.ofHours(24);
        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minus(duration);
        List<TelegramMessageEntity> messages = telegramMessageRepository.findMessagesForRecap(chatId, from, to);

        if (parts.length == 1) {

            llmService.generateRecap(messages, null)
                    .thenAccept(result -> {
                        telegramApiClient.sendMessage(senderId, "<b>Recap delle ultime 24 ore</b>\n"+result);
                    });

        }
        else{
            String keyword = parts[1].trim();

            llmService.generateRecap(messages, keyword)
                    .thenAccept(result -> {
                        telegramApiClient.sendMessage(senderId, "<b>Recap delle ultime 24 ore per: "+keyword+"</b>\n"+result);
                    });

        }

        return;


    }










    private void saveMessage(TelegramMessageDto dto) {
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
        telegramMessageRepository.save(savedMessage);
    }

}
