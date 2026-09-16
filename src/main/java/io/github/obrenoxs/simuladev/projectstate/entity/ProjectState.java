package io.github.obrenoxs.simuladev.projectstate.entity;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "project_states")
public class ProjectState {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> state;

    @OneToOne
    @JoinColumn(name = "company_link_id")
    private CompanyLink companyLink;

    public ProjectState() {
    }

    public ProjectState(UUID id, Map<String, Object> state, CompanyLink companyLink) {
        this.id = id;
        this.state = state;
        this.companyLink = companyLink;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public void setState(Map<String, Object> state) {
        this.state = state;
    }

    public CompanyLink getCompanyLink() {
        return companyLink;
    }

    public void setCompanyLink(CompanyLink companyLink) {
        this.companyLink = companyLink;
    }
}
