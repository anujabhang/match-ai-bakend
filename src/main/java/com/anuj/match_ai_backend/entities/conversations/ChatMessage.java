package com.anuj.match_ai_backend.entities.conversations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ChatMessage(
        String messageText,
        LocalDateTime time,
        String authorId
) {
}
