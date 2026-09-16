package io.github.obrenoxs.simuladev.projectstate.service;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.projectstate.entity.ProjectState;
import io.github.obrenoxs.simuladev.projectstate.repository.ProjectStateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectStateService {

    private final ProjectStateRepository projectStateRepository;

    public ProjectStateService(ProjectStateRepository projectStateRepository) {
        this.projectStateRepository = projectStateRepository;
    }

    @Transactional
    public ProjectState createEmpty(CompanyLink companyLink) {
        ProjectState projectState = new ProjectState();
        projectState.setCompanyLink(companyLink);

        return projectStateRepository.save(projectState);
    }
}
