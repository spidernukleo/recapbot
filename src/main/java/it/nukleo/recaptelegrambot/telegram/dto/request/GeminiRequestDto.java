package it.nukleo.recaptelegrambot.telegram.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class GeminiRequestDto {
    private List<Content> contents;

    public GeminiRequestDto(String prompt) {
        this.contents = List.of(
                new Content(
                        "user",
                        List.of(new Part(prompt))
                )
        );
    }

    @Data
    @AllArgsConstructor
    public static class Content {
        private String role;
        private List<Part> parts;
    }

    @Data
    @AllArgsConstructor
    public static class Part {
        private String text;
    }
}
