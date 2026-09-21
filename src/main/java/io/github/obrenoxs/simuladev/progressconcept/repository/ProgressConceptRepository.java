package io.github.obrenoxs.simuladev.progressconcept.repository;

import io.github.obrenoxs.simuladev.progressconcept.entity.ProgressConcept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProgressConceptRepository extends JpaRepository<ProgressConcept, UUID> {

     Optional<ProgressConcept> findByUserIdAndConceptId(UUID userId, UUID conceptId);
}
