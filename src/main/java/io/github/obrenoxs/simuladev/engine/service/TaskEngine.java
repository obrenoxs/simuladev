package io.github.obrenoxs.simuladev.engine.service;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.concept.repository.ConceptRepository;
import io.github.obrenoxs.simuladev.engine.exception.NoEligibleConceptsException;
import io.github.obrenoxs.simuladev.engine.result.EngineResult;
import io.github.obrenoxs.simuladev.progressconcept.entity.ProgressConcept;
import io.github.obrenoxs.simuladev.progressconcept.service.ProgressConceptService;
import io.github.obrenoxs.simuladev.projectstate.entity.ProjectState;
import io.github.obrenoxs.simuladev.projectstate.repository.ProjectStateRepository;
import io.github.obrenoxs.simuladev.shared.exception.ResourceNotFoundException;
import io.github.obrenoxs.simuladev.task.entity.Task;
import io.github.obrenoxs.simuladev.task.enums.TaskDifficulty;
import io.github.obrenoxs.simuladev.task.enums.TaskType;
import io.github.obrenoxs.simuladev.task.repository.TaskRepository;
import io.github.obrenoxs.simuladev.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskEngine {

    private final ProjectStateRepository projectStateRepository;
    private final ConceptRepository conceptRepository;
    private final ProgressConceptService progressConceptService;
    private final TaskRepository taskRepository;

    public TaskEngine(ProjectStateRepository projectStateRepository,
                      ConceptRepository conceptRepository,
                      ProgressConceptService progressConceptService,
                      TaskRepository taskRepository) {
        this.projectStateRepository = projectStateRepository;
        this.conceptRepository = conceptRepository;
        this.progressConceptService = progressConceptService;
        this.taskRepository = taskRepository;
    }

    public EngineResult nextTask(User user, CompanyLink companyLink) {

        ProjectState projectState = projectStateRepository.findByCompanyLinkId(companyLink.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado"));

        EngineResult finalEngineResult;
        if (projectState.getState() == null) {
            Concept concept = companyLink.getCompanyType().getFoundationalConcept();

            EngineResult result = new EngineResult(concept, TaskType.FEATURE, concept.getConceptName(), TaskDifficulty.EASY);
            return result;
        } else {
            List<Concept> listConcept = conceptRepository.findAllByStackAndTargetLevel(user.getStack(), user.getCurrentLevel());

            List<Concept> listEligible = listConcept.stream().filter(concept -> isEligible(concept, user)).toList();

            Map<Concept, Double> scores = listEligible.stream()
                    .collect(Collectors.toMap(
                            concept -> concept,
                            concept -> calculateScore(concept, user, companyLink)
                    ));

            List<Map.Entry<Concept, Double>> top3 = scores.entrySet().stream()
                    .sorted(Map.Entry.<Concept, Double>comparingByValue().reversed())
                    .limit(3)
                    .toList();

            double totalScore = top3.stream()
                    .mapToDouble(entry -> entry.getValue())
                    .sum();

            double draw = Math.random() * totalScore;

            double accumulated = 0.0;
            Concept selected = null;

            for (Map.Entry<Concept, Double> entry : top3) {
                accumulated += entry.getValue();
                if (accumulated >= draw) {
                    selected = entry.getKey();
                    break;
                }
            }

            if (selected == null) {
                throw new NoEligibleConceptsException("Nenhum conceito elegível disponível no momento para este nível/stack");
            }

            Optional<Task> lastTask = taskRepository.findTop1ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId());

            Concept finalSelected = selected;
            TaskType chosenType = finalSelected.getTaskTypes().stream()
                    .filter(type -> lastTask.isEmpty() || !type.equals(lastTask.get().getType()))
                    .findFirst()
                    .orElseGet(() -> finalSelected.getTaskTypes().iterator().next());

            if (finalSelected.getWeight() <= 3) {
                finalEngineResult = new EngineResult(finalSelected, chosenType, finalSelected.getConceptName(), TaskDifficulty.EASY);
            } else if (finalSelected.getWeight() > 3 && finalSelected.getWeight() <= 6) {
                finalEngineResult = new EngineResult(finalSelected, chosenType, finalSelected.getConceptName(), TaskDifficulty.MEDIUM);
            } else {
                finalEngineResult = new EngineResult(finalSelected, chosenType, finalSelected.getConceptName(), TaskDifficulty.HARD);
            }
        }
        return finalEngineResult;
    }

    private double calculateScore(Concept concept, User user, CompanyLink companyLink) {
        ProgressConcept progressConcept = progressConceptService.findOrCreate(user,concept);

        double fatorGap;
        if (!progressConcept.isCovered()) {
            fatorGap = 3.0;
        } else if (progressConcept.getRelatedTasks() == 1) {
            fatorGap = 1.5;
        } else {
            fatorGap = 0.5;
        }

        List<Task> taskList = taskRepository.findTop3ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId());
        boolean recentlyAppeared = taskList.stream().anyMatch(task -> task.getConcept().equals(concept));

        double fatorRecencia;
        if (recentlyAppeared) {
            fatorRecencia = 0.1;
        } else {
            fatorRecencia = 1.0;
        }

        return fatorGap * fatorRecencia * concept.getWeight();
    }

    private boolean isEligible(Concept concept, User user) {
        boolean eligibility = concept.getPrerequisites().stream()
                .allMatch(prerequisite -> progressConceptService.findOrCreate(user, prerequisite).isCovered());

        return eligibility;
    }
}
