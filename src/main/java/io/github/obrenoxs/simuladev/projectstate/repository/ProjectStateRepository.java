package io.github.obrenoxs.simuladev.projectstate.repository;

import io.github.obrenoxs.simuladev.projectstate.entity.ProjectState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectStateRepository extends JpaRepository<ProjectState, UUID> {

    Optional<ProjectState> findByCompanyLinkId(UUID companyLinkId);
}
