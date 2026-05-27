package it.nukleo.recaptelegrambot.telegram.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramGetFileResponseDto {
    private TelegramFileDto result;
}
