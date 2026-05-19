package it.nukleo.recaptelegrambot.llm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GeminiResponseDto {
    private List<Candidate> candidates;

    public String extractText() {
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }

        Candidate candidate = candidates.getFirst();
        if (candidate.getContent() == null || candidate.getContent().getParts() == null || candidate.getContent().getParts().isEmpty()) {
            return null;
        }

        Part part = candidate.getContent().getParts().getFirst();
        return part != null ? part.getText() : null;
    }

    @Data
    @AllArgsConstructor
    public static class Candidate {
        private Content content;
    }

    @Data
    @AllArgsConstructor
    public static class Content {
        private List<Part> parts;
    }

    @Data
    @AllArgsConstructor
    public static class Part {
        private String text;
    }
}
