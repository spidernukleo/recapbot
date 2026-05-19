package it.nukleo.recaptelegrambot.telegram.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramSendMessageDto {

    @JsonProperty("chat_id")
    private Long chatId;

    private String text;

    @JsonProperty("parse_mode")
    private String parseMode = "Markdown";
}
