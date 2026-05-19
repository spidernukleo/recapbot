package it.nukleo.recaptelegrambot.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramUserDto {
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("is_bot")
    private Boolean isBot;
}
