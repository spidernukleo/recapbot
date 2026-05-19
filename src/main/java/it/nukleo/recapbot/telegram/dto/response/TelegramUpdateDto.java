package it.nukleo.recapbot.telegram.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramUpdateDto {
    @JsonProperty("update_id")
    private Long updateId;

    private TelegramMessageDto message;

    //leggere altri tipi di evento?
}
