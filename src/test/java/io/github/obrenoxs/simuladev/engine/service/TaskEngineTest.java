package io.github.obrenoxs.simuladev.engine.service;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.concept.repository.ConceptRepository;
import io.github.obrenoxs.simuladev.engine.result.EngineResult;
import io.github.obrenoxs.simuladev.engine.util.RandomGenerator;
import io.github.obrenoxs.simuladev.progressconcept.service.ProgressConceptService;
import io.github.obrenoxs.simuladev.projectstate.entity.ProjectState;
import io.github.obrenoxs.simuladev.projectstate.repository.ProjectStateRepository;
import io.github.obrenoxs.simuladev.shared.exception.ResourceNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

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
}
