package it.nukleo.recaptelegrambot.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix="local")
public class LocalLlmProperties {

    @NotBlank
    private String cliPath;

    @NotBlank
    private String modelPath;

    @NotBlank
    private String language;

    @NotBlank
    private String ffmpegPath;

}
