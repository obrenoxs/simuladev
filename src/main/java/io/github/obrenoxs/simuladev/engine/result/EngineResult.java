package io.github.obrenoxs.simuladev.engine.result;

import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.task.enums.TaskDifficulty;
import io.github.obrenoxs.simuladev.task.enums.TaskType;

public record EngineResult(
        Concept concept,
        TaskType type,
        String targetEntity,
        TaskDifficulty difficulty
) {}
