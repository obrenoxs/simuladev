package io.github.obrenoxs.simuladev.concept.entity;

import jakarta.persistence.*;

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
}
