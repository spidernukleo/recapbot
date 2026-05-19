package it.nukleo.recaptelegrambot.telegram.repository;

import it.nukleo.recaptelegrambot.telegram.entity.TelegramMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramMessageRepository extends JpaRepository<TelegramMessageEntity, Long> {
}
