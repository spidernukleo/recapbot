package it.nukleo.recaptelegrambot.telegram.persistence.repository;

import it.nukleo.recaptelegrambot.telegram.persistence.entity.TelegramMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramMessageRepository extends JpaRepository<TelegramMessageEntity, Long> {
}
