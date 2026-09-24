package io.github.obrenoxs.simuladev.projectstate.service;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.projectstate.entity.ProjectState;
import io.github.obrenoxs.simuladev.projectstate.repository.ProjectStateRepository;
import io.github.obrenoxs.simuladev.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

    @Transactional(readOnly = true)
    public ProjectState findByCompanyLink(CompanyLink companyLink) {
        ProjectState projectState = projectStateRepository.findByCompanyLinkId(companyLink.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Estado do projeto não encontrado"));

        return projectState;
    }

    @Transactional
    public void deliver(Concept concept, CompanyLink companyLink) {
        ProjectState projectState = findByCompanyLink(companyLink);

        if (projectState.getState() == null) {
            projectState.setState(new HashMap<>());
        }

        List<String> concepts = (List<String>) projectState.getState().getOrDefault("conceitosImplementados", new ArrayList<>());
        concepts.add(concept.getConceptName());
        projectState.getState().put("conceitosImplementados", concepts);

        projectStateRepository.save(projectState);
    }
}
