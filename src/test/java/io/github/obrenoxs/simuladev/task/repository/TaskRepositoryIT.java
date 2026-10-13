package io.github.obrenoxs.simuladev.task.repository;

import io.github.obrenoxs.simuladev.AbstractIntegrationTest;
import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.companylink.repository.CompanyLinkRepository;
import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import io.github.obrenoxs.simuladev.companytype.repository.CompanyTypeRepository;
import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.concept.repository.ConceptRepository;
import io.github.obrenoxs.simuladev.task.entity.Task;
import io.github.obrenoxs.simuladev.task.enums.TaskDifficulty;
import io.github.obrenoxs.simuladev.task.enums.TaskStatus;
import io.github.obrenoxs.simuladev.task.enums.TaskType;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.enums.UserRole;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
public class TaskRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyLinkRepository companyLinkRepository;

    @Autowired
    private CompanyTypeRepository companyTypeRepository;

    @Autowired
    private ConceptRepository conceptRepository;

    @Test
    void findTop3ShouldReturnThreeMostRecentTasksNewestFirst() {

        User user = userRepository.save(new User(null, "UserTest", "it-user@example.com",
                "Java + Spring", "ESTAGIARIO", 0, UserRole.USER, "hash"));

        CompanyType companyType = companyTypeRepository.findAll().get(0);

        Concept concept = conceptRepository.findAll().get(0);

        CompanyLink companyLink = companyLinkRepository.save(new CompanyLink(null, "Test Company",
                LocalDate.now(), true, user, companyType));

        LocalDateTime base = LocalDateTime.of(2026, 1, 1, 10, 0);

        taskRepository.save(buildTask(companyLink, concept, base.plusHours(1)));
        taskRepository.save(buildTask(companyLink, concept, base.plusHours(3)));
        taskRepository.save(buildTask(companyLink, concept, base));
        taskRepository.save(buildTask(companyLink, concept, base.plusHours(2)));

        List<Task> result = taskRepository.findTop3ByCompanyLinkIdOrderByCreatedAtDesc(companyLink.getId());

        assertEquals(3, result.size());
        assertEquals(base.plusHours(3), result.get(0).getCreatedAt());
        assertEquals(base.plusHours(2), result.get(1).getCreatedAt());
        assertEquals(base.plusHours(1), result.get(2).getCreatedAt());
    }

    @Test
    void findTop3ShouldIgnoreTasksFromOtherCompanyLinks() {

        User user = userRepository.save(new User(null, "UserTest", "it-user@example.com",
                "Java + Spring", "ESTAGIARIO", 0, UserRole.USER, "hash"));

        CompanyType companyType = companyTypeRepository.findAll().get(0);

        Concept concept = conceptRepository.findAll().get(0);

        CompanyLink linkA = companyLinkRepository.save(new CompanyLink(null, "Company A",
                LocalDate.now(), true, user, companyType));

        CompanyLink linkB = companyLinkRepository.save(new CompanyLink(null, "Company B",
                LocalDate.now(), true, user, companyType));

        LocalDateTime base = LocalDateTime.of(2026, 1, 1, 10, 0);

        taskRepository.save(buildTask(linkA, concept, base));
        taskRepository.save(buildTask(linkB, concept, base.plusHours(5)));

        List<Task> result = taskRepository.findTop3ByCompanyLinkIdOrderByCreatedAtDesc(linkA.getId());

        assertEquals(1, result.size());
        assertEquals(linkA.getId(), result.get(0).getCompanyLink().getId());
    }

    private Task buildTask(CompanyLink companyLink, Concept concept, LocalDateTime createdAt) {
        Task task = new Task();
        task.setCompanyLink(companyLink);
        task.setConcept(concept);
        task.setType(TaskType.FEATURE);
        task.setDifficulty(TaskDifficulty.EASY);
        task.setStatus(TaskStatus.PENDING);
        task.setTicketText("ticket");
        task.setCreatedAt(createdAt);
        return task;
    }
}
