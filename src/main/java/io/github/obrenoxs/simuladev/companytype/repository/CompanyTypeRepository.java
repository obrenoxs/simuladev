package io.github.obrenoxs.simuladev.companytype.repository;

import io.github.obrenoxs.simuladev.companytype.entity.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyTypeRepository extends JpaRepository<CompanyType, UUID> {
}
