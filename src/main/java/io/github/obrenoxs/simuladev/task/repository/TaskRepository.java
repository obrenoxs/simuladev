package io.github.obrenoxs.simuladev.task.repository;

import io.github.obrenoxs.simuladev.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findTop1ByCompanyLinkIdOrderByCreatedAtDesc(UUID id);

    List<Task> findTop3ByCompanyLinkIdOrderByCreatedAtDesc(UUID id);
}
