package io.github.obrenoxs.simuladev.progressconcept.entity;

import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.user.entity.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "progress_concepts")
public class ProgressConcept {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private boolean covered;
    private int relatedTasks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "concept_id")
    private Concept concept;

    public ProgressConcept() {
    }

    public ProgressConcept(UUID id, boolean covered, int relatedTasks, User user, Concept concept) {
        this.id = id;
        this.covered = covered;
        this.relatedTasks = relatedTasks;
        this.user = user;
        this.concept = concept;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public int getRelatedTasks() {
        return relatedTasks;
    }

    public void setRelatedTasks(int relatedTasks) {
        this.relatedTasks = relatedTasks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }
}
