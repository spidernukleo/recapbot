package it.nukleo.recaptelegrambot.telegram.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramEditMessageDto {

    @JsonProperty("chat_id")
    private Long chatId;

    @JsonProperty("message_id")
    private Long messageId;

    private String text;

    @JsonProperty("parse_mode")
    private String parseMode = "HTML";
}