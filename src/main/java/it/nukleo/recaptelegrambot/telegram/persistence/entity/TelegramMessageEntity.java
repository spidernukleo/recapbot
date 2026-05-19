package it.nukleo.recaptelegrambot.telegram.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "telegram_message",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_message_chat",
                        columnNames = {"chat_id", "message_id"}
                )
        },
        indexes = {
                @Index(name = "idx_message_chat_sent_at", columnList = "chat_id,sent_at"),
                @Index(name = "idx_message_sent_at", columnList = "sent_at"),
                @Index(name = "idx_message_user_id", columnList = "user_id")
        }
)
@Entity
public class TelegramMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", nullable = false)
    private Long messageId;

    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    //@JoinColumn(name = "chat_id", nullable = false)
    @Column(name="chat_id", nullable = false)
    private Long chatId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_first_name")
    private String userFirstName;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "reply_to_message_id")
    private Long replyToMessageId;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;
}
