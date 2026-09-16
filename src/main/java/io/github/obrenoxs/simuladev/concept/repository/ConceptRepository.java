package io.github.obrenoxs.simuladev.concept.repository;

import io.github.obrenoxs.simuladev.concept.entity.Concept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConceptRepository extends JpaRepository<Concept, UUID> {

    List<Concept> findAllByStackAndTargetLevel(String stack, String targetLevel);
}
