package it.nukleo.recaptelegrambot.telegram.dto.response;


import lombok.Data;

@Data
public class TelegramReactionEmojiDto {

    private String type="emoji";

    private String emoji;

    public TelegramReactionEmojiDto(String emoji) {
        this.emoji = emoji;
    }
}
