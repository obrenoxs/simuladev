package io.github.obrenoxs.simuladev.engine.service;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.concept.repository.ConceptRepository;
import io.github.obrenoxs.simuladev.engine.exception.NoEligibleConceptsException;
import io.github.obrenoxs.simuladev.engine.result.EngineResult;
import io.github.obrenoxs.simuladev.engine.util.RandomGenerator;
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
import io.github.obrenoxs.simuladev.user.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskEngineTest {

    @InjectMocks
    private TaskEngine taskEngine;

    @Mock
    private ProjectStateRepository projectStateRepository;

    @Mock
    private ConceptRepository conceptRepository;

    @Mock
    private ProgressConceptService progressConceptService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RandomGenerator randomGenerator;

    private Concept foundationalConcept;
    private CompanyType companyType;

    private User user;
    private CompanyLink companyLink;
    private ProjectState projectState;

    private Concept conceptA;
    private Concept conceptB;

    private ProgressConcept progressA;
    private ProgressConcept progressB;

    @BeforeEach
    void setUp() throws Exception {
        foundationalConcept = new Concept(
                UUID.randomUUID(),
                "Java + Spring",
                "Segurança",
                "Cadastro e Login",
                5,
                "ESTAGIARIO"
        );

        companyType = new CompanyType(UUID.randomUUID(), "PRESTADORA DE SERVIÇOS", "Empresa que presta serviços de tecnologia");
        companyType.setFoundationalConcept(foundationalConcept);

        user = new User(UUID.randomUUID(), "UserTest", "userTest@example.com", "Java + Spring", "ESTAGIARIO", 0, UserRole.USER, "12345678");
        companyLink = new CompanyLink(UUID.randomUUID(), "Test Company", LocalDate.now(), true, user, companyType);
        projectState = new ProjectState(UUID.randomUUID(), null, companyLink);

        conceptA = new Concept(UUID.randomUUID(), "Java + Spring", "Persistência", "Query method Spring Data", 6, "ESTAGIARIO");
        conceptA.getTaskTypes().add(TaskType.FEATURE);

        conceptB = new Concept(UUID.randomUUID(), "Java + Spring", "Validação", "Bean Validation", 4, "ESTAGIARIO");
        conceptB.getTaskTypes().add(TaskType.FEATURE);

        progressA = new ProgressConcept(UUID.randomUUID(), false, 0, user, conceptA);
        progressB = new ProgressConcept(UUID.randomUUID(), false, 0, user, conceptB);
    }

    @Test
    void nextTaskShouldReturnConceptFoundationalWhenProjectStateIsEmpty() {

        when(projectStateRepository.findByCompanyLinkId(companyLink.getId()))
                .thenReturn(Optional.of(projectState));

        EngineResult result = taskEngine.nextTask(user, companyLink);

        Assertions.assertEquals(foundationalConcept, result.concept());
        Assertions.assertEquals(TaskType.FEATURE, result.type());
        Assertions.assertEquals(TaskDifficulty.EASY, result.difficulty());
    }

    @Test
    void nextTaskShouldThrowResourceNotFoundWhenProjectStateIsEmpty() {

        when(projectStateRepository.findByCompanyLinkId(companyLink.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            EngineResult result = taskEngine.nextTask(user, companyLink);
        });
    }

    @Test
    void nextTaskShouldReturnConceptAWhenDrawFavorsConceptA() {

        projectState.setState(new HashMap<>());

        when(projectStateRepository.findByCompanyLinkId(companyLink.getId()))
                .thenReturn(Optional.of(projectState));

        when(conceptRepository.findAllByStackAndTargetLevel(user.getStack(), user.getCurrentLevel()))
                .thenReturn(List.of(conceptA, conceptB));

        when(progressConceptService.findOrCreate(user, conceptA))
                .thenReturn(progressA);

        when(progressConceptService.findOrCreate(user, conceptB))
                .thenReturn(progressB);

        when(taskRepository.findTop3ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(List.of());

        when(taskRepository.findTop1ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(Optional.empty());

        when(randomGenerator.nextDouble())
                .thenReturn(0.3);

        EngineResult result = taskEngine.nextTask(user, companyLink);

        Assertions.assertEquals(conceptA, result.concept());
        Assertions.assertEquals(TaskType.FEATURE, result.type());
        Assertions.assertEquals(TaskDifficulty.MEDIUM, result.difficulty());
    }

    @Test
    void nextTaskShouldReturnConceptBWhenDrawFavorsConceptB() {

        projectState.setState(new HashMap<>());

        when(projectStateRepository.findByCompanyLinkId(companyLink.getId()))
                .thenReturn(Optional.of(projectState));

        when(conceptRepository.findAllByStackAndTargetLevel(user.getStack(), user.getCurrentLevel()))
                .thenReturn(List.of(conceptA, conceptB));

        when(progressConceptService.findOrCreate(user, conceptA))
                .thenReturn(progressA);

        when(progressConceptService.findOrCreate(user, conceptB))
                .thenReturn(progressB);

        when(taskRepository.findTop3ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(List.of());

        when(taskRepository.findTop1ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(Optional.empty());

        when(randomGenerator.nextDouble())
                .thenReturn(0.8);

        EngineResult result = taskEngine.nextTask(user, companyLink);

        Assertions.assertEquals(conceptB, result.concept());
        Assertions.assertEquals(TaskType.FEATURE, result.type());
        Assertions.assertEquals(TaskDifficulty.MEDIUM, result.difficulty());
    }

    @Test
    void nextTaskShouldReturnNoEligibleConceptsExceptionWhenNoConceptIsEligible() {

        projectState.setState(new HashMap<>());

        conceptB.getPrerequisites().add(conceptA);

        when(projectStateRepository.findByCompanyLinkId(companyLink.getId()))
                .thenReturn(Optional.of(projectState));

        when(conceptRepository.findAllByStackAndTargetLevel(user.getStack(), user.getCurrentLevel()))
                .thenReturn(List.of(conceptB));

        when(progressConceptService.findOrCreate(user, conceptA))
                .thenReturn(progressA);

        Assertions.assertThrows(NoEligibleConceptsException.class, () -> {
            EngineResult result = taskEngine.nextTask(user, companyLink);
        });
    }

    @Test
    void nextTaskShouldReturnConceptWhenPreRequisiteIsCovered() {

        projectState.setState(new HashMap<>());

        conceptB.getPrerequisites().add(conceptA);
        progressA.setCovered(true);

        when(projectStateRepository.findByCompanyLinkId(companyLink.getId()))
                .thenReturn(Optional.of(projectState));

        when(conceptRepository.findAllByStackAndTargetLevel(user.getStack(), user.getCurrentLevel()))
                .thenReturn(List.of(conceptB));

        when(progressConceptService.findOrCreate(user, conceptA))
                .thenReturn(progressA);

        when(progressConceptService.findOrCreate(user, conceptB))
                .thenReturn(progressB);

        when(taskRepository.findTop3ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(List.of());

        when(randomGenerator.nextDouble())
                .thenReturn(0.5);

        when(taskRepository.findTop1ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(Optional.empty());

        EngineResult result = taskEngine.nextTask(user, companyLink);

        Assertions.assertEquals(conceptB, result.concept());
        Assertions.assertEquals(TaskType.FEATURE, result.type());
        Assertions.assertEquals(TaskDifficulty.MEDIUM, result.difficulty());
    }

    @Test
    void nextTaskShouldThrowNoEligibleConceptsWhenOnlyOnePrerequisiteIsCovered() {

        projectState.setState(new HashMap<>());

        Concept conceptC = new Concept(UUID.randomUUID(), "Java + Spring", "Segurança", "Autenticação JWT", 5, "ESTAGIARIO");
        ProgressConcept progressC = new ProgressConcept(UUID.randomUUID(), false, 0, user, conceptC);

        conceptB.getPrerequisites().add(conceptA);
        conceptB.getPrerequisites().add(conceptC);
        progressA.setCovered(true);

        when(projectStateRepository.findByCompanyLinkId(companyLink.getId()))
                .thenReturn(Optional.of(projectState));

        when(conceptRepository.findAllByStackAndTargetLevel(user.getStack(), user.getCurrentLevel()))
                .thenReturn(List.of(conceptB));

        when(progressConceptService.findOrCreate(user, conceptA))
                .thenReturn(progressA);

        when(progressConceptService.findOrCreate(user, conceptC))
                .thenReturn(progressC);

        lenient().when(progressConceptService.findOrCreate(user, conceptA))
                .thenReturn(progressA);

        Assertions.assertThrows(NoEligibleConceptsException.class, () -> {
            EngineResult result = taskEngine.nextTask(user, companyLink);
        });
    }

    @Test
    void nextTaskShouldPreferConceptBWhenConceptAAppearedRecently() {

        projectState.setState(new HashMap<>());

        Task recentTask = new Task();
        recentTask.setConcept(conceptA);

        when(projectStateRepository.findByCompanyLinkId(companyLink.getId()))
                .thenReturn(Optional.of(projectState));

        when(conceptRepository.findAllByStackAndTargetLevel(user.getStack(), user.getCurrentLevel()))
                .thenReturn(List.of(conceptA, conceptB));

        when(progressConceptService.findOrCreate(user, conceptA))
                .thenReturn(progressA);

        when(progressConceptService.findOrCreate(user, conceptB))
                .thenReturn(progressB);

        when(taskRepository.findTop3ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(List.of(recentTask));

        when(randomGenerator.nextDouble())
                .thenReturn(0.3);

        when(taskRepository.findTop1ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(Optional.empty());

        EngineResult result = taskEngine.nextTask(user, companyLink);

        Assertions.assertEquals(conceptB, result.concept());
        Assertions.assertEquals(TaskType.FEATURE, result.type());
        Assertions.assertEquals(TaskDifficulty.MEDIUM, result.difficulty());
    }

    @Test
    void nextTaskShouldChooseBugFixWhenLastTaskWasFeature() {

        projectState.setState(new HashMap<>());

        conceptA.getTaskTypes().add(TaskType.BUGFIX);

        Task lastTask = new Task();
        lastTask.setType(TaskType.FEATURE);

        when(projectStateRepository.findByCompanyLinkId(companyLink.getId()))
                .thenReturn(Optional.of(projectState));

        when(conceptRepository.findAllByStackAndTargetLevel(user.getStack(), user.getCurrentLevel()))
                .thenReturn(List.of(conceptA));

        when(progressConceptService.findOrCreate(user, conceptA))
                .thenReturn(progressA);

        when(taskRepository.findTop3ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(List.of());

        when(randomGenerator.nextDouble())
                .thenReturn(0.5);

        when(taskRepository.findTop1ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId()))
                .thenReturn(Optional.of(lastTask));

        EngineResult result = taskEngine.nextTask(user, companyLink);

        Assertions.assertEquals(conceptA, result.concept());
        Assertions.assertEquals(TaskType.BUGFIX, result.type());
    }
}
