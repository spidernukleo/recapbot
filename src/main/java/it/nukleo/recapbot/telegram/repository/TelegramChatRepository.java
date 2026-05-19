package it.nukleo.recapbot.telegram.repository;

import it.nukleo.recapbot.telegram.entity.TelegramChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramChatRepository extends JpaRepository<TelegramChatEntity, Long> {
    Optional<TelegramChatEntity> findByChatId(Long id);
}