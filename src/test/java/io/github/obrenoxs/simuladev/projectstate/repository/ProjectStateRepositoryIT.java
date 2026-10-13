package io.github.obrenoxs.simuladev.projectstate.repository;

import io.github.obrenoxs.simuladev.AbstractIntegrationTest;
import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.companylink.repository.CompanyLinkRepository;
import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import io.github.obrenoxs.simuladev.companytype.repository.CompanyTypeRepository;
import io.github.obrenoxs.simuladev.projectstate.entity.ProjectState;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.enums.UserRole;
import io.github.obrenoxs.simuladev.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Transactional
public class ProjectStateRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private ProjectStateRepository projectStateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyLinkRepository companyLinkRepository;

    @Autowired
    private CompanyTypeRepository companyTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void stateShouldBeStoredAndReadBackAsJson() {

        CompanyLink companyLink = createCompanyLink();
        Map<String, Object> state = Map.of("conceitosImplementados", List.of("Cadastro e Login"));

        projectStateRepository.save(new ProjectState(null, state, companyLink));

        entityManager.flush();
        entityManager.clear();

        ProjectState reloaded = projectStateRepository.findByCompanyLinkId(companyLink.getId()).orElseThrow();

        assertEquals(state, reloaded.getState());
    }

    @Test
    void emptyStateShouldBeReadBackAsNull() {

        CompanyLink companyLink = createCompanyLink();

        projectStateRepository.save(new ProjectState(null, null, companyLink));

        entityManager.flush();
        entityManager.clear();

        ProjectState reloaded = projectStateRepository.findByCompanyLinkId(companyLink.getId()).orElseThrow();

        assertNull(reloaded.getState());
    }

    private CompanyLink createCompanyLink() {
        User user = userRepository.save(new User(null, "UserTest", "it-user@example.com",
                "Java + Spring", "ESTAGIARIO", 0, UserRole.USER, "hash"));

        CompanyType companyType = companyTypeRepository.findAll().get(0);

        return companyLinkRepository.save(new CompanyLink(null, "Test Company",
                LocalDate.now(), true, user, companyType));
    }
}
