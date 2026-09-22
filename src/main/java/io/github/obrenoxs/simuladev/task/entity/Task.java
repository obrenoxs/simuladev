package io.github.obrenoxs.simuladev.task.entity;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.concept.entity.Concept;
import io.github.obrenoxs.simuladev.task.enums.TaskDifficulty;
import io.github.obrenoxs.simuladev.task.enums.TaskStatus;
import io.github.obrenoxs.simuladev.task.enums.TaskType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_link_id")
    private CompanyLink companyLink;

    @ManyToOne
    @JoinColumn(name = "concept_id")
    private Concept concept;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Enumerated(EnumType.STRING)
    private TaskDifficulty difficulty;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private String ticketText;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;

    public Task() {
    }

    public Task(
            UUID id,
            CompanyLink companyLink,
            Concept concept,
            TaskType type,
            TaskDifficulty difficulty,
            TaskStatus status,
            String ticketText,
            LocalDateTime createdAt,
            LocalDateTime deliveredAt
    ) {
        this.id = id;
        this.companyLink = companyLink;
        this.concept = concept;
        this.type = type;
        this.difficulty = difficulty;
        this.status = status;
        this.ticketText = ticketText;
        this.createdAt = createdAt;
        this.deliveredAt = deliveredAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CompanyLink getCompanyLink() {
        return companyLink;
    }

    public void setCompanyLink(CompanyLink companyLink) {
        this.companyLink = companyLink;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(TaskDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTicketText() {
        return ticketText;
    }

    public void setTicketText(String ticketText) {
        this.ticketText = ticketText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
}