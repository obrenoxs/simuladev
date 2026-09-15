package io.github.obrenoxs.simuladev.companylink.repository;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompanyLinkRepository extends JpaRepository<CompanyLink, UUID> {

    List<CompanyLink> findAllByUserId(UUID userId);
}
