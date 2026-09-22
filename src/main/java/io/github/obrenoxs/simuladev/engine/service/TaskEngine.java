package io.github.obrenoxs.simuladev.engine.service;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.engine.result.EngineResult;
import io.github.obrenoxs.simuladev.projectstate.entity.ProjectState;
import io.github.obrenoxs.simuladev.projectstate.repository.ProjectStateRepository;
import io.github.obrenoxs.simuladev.shared.exception.ResourceNotFoundException;
import io.github.obrenoxs.simuladev.task.enums.TaskDifficulty;
import io.github.obrenoxs.simuladev.task.enums.TaskType;
import io.github.obrenoxs.simuladev.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class TaskEngine {

    private final ProjectStateRepository projectStateRepository;

    public TaskEngine(ProjectStateRepository projectStateRepository) {
        this.projectStateRepository = projectStateRepository;
    }

    public EngineResult nextTask(User user, CompanyLink companyLink) {

        ProjectState projectState = projectStateRepository.findByCompanyLinkId(companyLink.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado"));

        if (projectState.getState() == null) {
            Concept concept = companyLink.getCompanyType().getFoundationalConcept();

            EngineResult result = new EngineResult(concept, TaskType.FEATURE, concept.getConceptName(), TaskDifficulty.EASY);
            return result;
        }

        return null;
    }
}
