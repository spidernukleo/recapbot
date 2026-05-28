package it.nukleo.recaptelegrambot.llm.web;

import it.nukleo.recaptelegrambot.config.LocalLlmProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service("localLlmClient")
public class LocalLlmClient implements LlmClient {

    private final LocalLlmProperties properties;

    public LocalLlmClient(LocalLlmProperties properties) {
        this.properties = properties;
    }

    @Override
    public CompletableFuture<String> generateTextFromPrompt(String prompt) {
        return null;
    }

    @Override
    @Async("voiceExecutor")
    public CompletableFuture<String> transcribeAudio(Path audioFile) throws Exception {
        Path wavFile = convertToWav(audioFile);
        return CompletableFuture.completedFuture(doTranscription(wavFile));
    }

    private Path convertToWav(Path audioFile) throws Exception {
        String fileName = audioFile.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        Path wavFile = audioFile.getParent().resolve(baseName + ".wav");

        ProcessBuilder pb = new ProcessBuilder(
                properties.getFfmpegPath(),
                "-y",
                "-i", audioFile.toAbsolutePath().toString(),
                "-ar", "16000",
                "-ac", "1",
                "-c:a", "pcm_s16le",
                wavFile.toAbsolutePath().toString()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IllegalStateException("ffmpeg failed with exit code " + exitCode);
        }
        Files.deleteIfExists(audioFile);

        return wavFile;
    }

    private String doTranscription(Path audioFile) throws Exception {
        String fileName = audioFile.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));

        Path outputBase = audioFile.getParent().resolve(baseName);
        Path txtFile = audioFile.getParent().resolve(baseName + ".txt");

        ProcessBuilder pb = new ProcessBuilder(
                properties.getCliPath(),
                "-m", properties.getModelPath(),
                "-f", audioFile.toAbsolutePath().toString(),
                "-l", properties.getLanguage(),
                "--output-txt",
                "-of", outputBase.toAbsolutePath().toString()
        );

        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            Files.deleteIfExists(audioFile);
            throw new IllegalStateException("whisper-cli failed with exit code " + exitCode);
        }

        String transcription;

        if(Files.exists(txtFile)){
            transcription = Files.readString(txtFile).trim();
            Files.delete(txtFile);
        }
        else{
            transcription = "Trascrizione non riuscita.";
        }

        Files.deleteIfExists(audioFile);

        return transcription;
    }
}
