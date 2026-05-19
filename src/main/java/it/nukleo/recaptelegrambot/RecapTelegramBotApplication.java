package it.nukleo.recaptelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RecapTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecapTelegramBotApplication.class, args);
	}

}
