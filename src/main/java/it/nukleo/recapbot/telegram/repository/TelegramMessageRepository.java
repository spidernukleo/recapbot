package it.nukleo.recapbot.telegram.repository;

import it.nukleo.recapbot.telegram.entity.TelegramMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramMessageRepository extends JpaRepository<TelegramMessageEntity, Long> {
}
