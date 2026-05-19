package it.nukleo.recaptelegrambot.repository;

import it.nukleo.recaptelegrambot.entity.TelegramMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramMessageRepository extends JpaRepository<TelegramMessageEntity, Long> {
}
