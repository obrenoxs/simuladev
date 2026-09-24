package io.github.obrenoxs.simuladev.progressconcept.service;

import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.progressconcept.entity.ProgressConcept;
import io.github.obrenoxs.simuladev.progressconcept.repository.ProgressConceptRepository;
import io.github.obrenoxs.simuladev.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressConceptService {

    private final ProgressConceptRepository progressConceptRepository;

    public ProgressConceptService(ProgressConceptRepository progressConceptRepository) {
        this.progressConceptRepository = progressConceptRepository;
    }

    @Transactional
    public ProgressConcept findOrCreate(User user, Concept concept) {
        ProgressConcept progressConcept = progressConceptRepository.findByUserIdAndConceptId(user.getId(), concept.getId())
                .orElseGet(() -> {
                    ProgressConcept newProgressConcept = new ProgressConcept();
                    newProgressConcept.setUser(user);
                    newProgressConcept.setConcept(concept);
                    newProgressConcept.setCovered(false);
                    newProgressConcept.setRelatedTasks(0);
                    return progressConceptRepository.save(newProgressConcept);
                });

        return progressConcept;
    }

    @Transactional
    public void markProgress(User user, Concept concept) {
        ProgressConcept progressConcept = findOrCreate(user, concept);
        progressConcept.setCovered(true);
        progressConcept.setRelatedTasks(progressConcept.getRelatedTasks() + 1);
        progressConceptRepository.save(progressConcept);
    }
}
