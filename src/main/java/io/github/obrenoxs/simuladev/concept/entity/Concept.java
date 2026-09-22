package io.github.obrenoxs.simuladev.concept.entity;

import io.github.obrenoxs.simuladev.task.enums.TaskType;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "concepts")
public class Concept {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String stack;
    private String category;
    private String conceptName;
    private Integer weight;
    private String targetLevel;

    @ManyToMany
    @JoinTable(
            name = "concept_prerequisites",
            joinColumns = @JoinColumn(name = "concept_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private Set<Concept> prerequisites = new HashSet<>();

    @ManyToMany(mappedBy = "prerequisites")
    private Set<Concept> unlockedConcepts = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "concept_task_types", joinColumns = @JoinColumn(name = "concept_id"))
    @Column(name = "task_type")
    @Enumerated(EnumType.STRING)
    private Set<TaskType> taskTypes = new HashSet<>();

    public Concept() {
    }

    public Concept(UUID id, String stack, String category, String conceptName, Integer weight, String targetLevel) {
        this.id = id;
        this.stack = stack;
        this.category = category;
        this.conceptName = conceptName;
        this.weight = weight;
        this.targetLevel = targetLevel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(String targetLevel) {
        this.targetLevel = targetLevel;
    }

    public Set<Concept> getPrerequisites() {
        return prerequisites;
    }

    public Set<Concept> getUnlockedConcepts() {
        return unlockedConcepts;
    }

    public Set<TaskType> getTaskTypes() {
        return taskTypes;
    }
}
