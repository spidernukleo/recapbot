package it.nukleo.recaptelegrambot.telegram.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramReactionEmojiDto;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramSendReactionDto {

    @JsonProperty("chat_id")
    private Long chatId;

    @JsonProperty("message_id")
    private Long messageId;

    private TelegramReactionEmojiDto[] reaction;
}
