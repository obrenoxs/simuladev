package io.github.obrenoxs.simuladev.task.dto.response;

import io.github.obrenoxs.simuladev.task.enums.TaskDifficulty;
import io.github.obrenoxs.simuladev.task.enums.TaskStatus;
import io.github.obrenoxs.simuladev.task.enums.TaskType;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String conceptName,
        TaskType type,
        TaskDifficulty difficulty,
        String ticketText,
        TaskStatus status,
        LocalDateTime createdAt
) {}
