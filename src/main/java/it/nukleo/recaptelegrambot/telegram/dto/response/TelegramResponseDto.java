package it.nukleo.recaptelegrambot.telegram.dto.response;


import lombok.Data;

@Data
public class TelegramResponseDto<T> {
    private boolean ok;
    private T result;
}
