package it.nukleo.recapbot.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix="telegram.bot")
@Validated
public class TelegramBotProperties {
    @NotBlank
    private String token;

    @NotBlank
    private String baseUrl;
}
