package it.nukleo.recaptelegrambot.telegram.web;

import it.nukleo.recaptelegrambot.config.TelegramBotProperties;
import it.nukleo.recaptelegrambot.telegram.dto.request.TelegramSendMessageDto;
import it.nukleo.recaptelegrambot.telegram.dto.request.TelegramSendReactionDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramGetFileResponseDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramReactionEmojiDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
public class TelegramApiClient {
    private final RestClient telegramRestClient;
    private final TelegramBotProperties properties;

    public TelegramApiClient(TelegramBotProperties properties) {
        this.properties = properties;
        this.telegramRestClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl() + "/bot" + properties.getToken())
                .build();
    }


    public void sendMessage(Long chatId,  String text) {
        TelegramSendMessageDto dto = new TelegramSendMessageDto();
        dto.setChatId(chatId);
        dto.setText(text);

        telegramRestClient.post()
                .uri("/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public String getFilePath(String fileId) {
        TelegramGetFileResponseDto response = telegramRestClient.get()
                .uri("/getFile?file_id={fileId}", fileId)
                .retrieve()
                .body(TelegramGetFileResponseDto.class);

        return response.getResult().getFilePath();
    }

    public byte[] downloadFile(String filePath) {
        byte[] file = telegramRestClient.get()
                .uri(URI.create(properties.getBaseUrl() + "/file/bot" + properties.getToken() + "/" + filePath))
                .retrieve()
                .body(byte[].class);

        return file;
    }


    public void sendReaction(Long chatId, Long messageId, String emoji) {
        TelegramSendReactionDto dto = new TelegramSendReactionDto();
        TelegramReactionEmojiDto emojiDto = new TelegramReactionEmojiDto(emoji);
        dto.setChatId(chatId);
        dto.setMessageId(messageId);
        dto.setReaction(new TelegramReactionEmojiDto[]{emojiDto});

        telegramRestClient.post()
                .uri("/setMessageReaction")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }
}
