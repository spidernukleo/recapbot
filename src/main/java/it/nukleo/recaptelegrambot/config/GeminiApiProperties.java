package it.nukleo.recaptelegrambot.config;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix="gemini.api")
@Validated
public class GeminiApiProperties {

    @NotBlank
    private String key;

    @NotBlank
    private String baseUrl;

    @NotBlank
    private String model;
}
