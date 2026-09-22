package io.github.obrenoxs.simuladev.companytype.entity;

import io.github.obrenoxs.simuladev.concept.entity.Concept;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "company_types")
public class CompanyType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String domainName;
    private String description;

    @ManyToOne
    @JoinColumn(name = "foundational_concept_id")
    private Concept foundationalConcept;

    public CompanyType() {
    }

    public CompanyType(UUID id, String domainName, String description) {
        this.id = id;
        this.domainName = domainName;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Concept getFoundationalConcept() {
        return foundationalConcept;
    }
}
