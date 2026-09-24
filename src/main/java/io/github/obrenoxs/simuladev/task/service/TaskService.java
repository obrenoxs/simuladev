package io.github.obrenoxs.simuladev.task.service;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.companylink.repository.CompanyLinkRepository;
import io.github.obrenoxs.simuladev.companylink.service.CompanyLinkService;
import io.github.obrenoxs.simuladev.engine.service.TaskEngine;
import io.github.obrenoxs.simuladev.shared.exception.ResourceNotFoundException;
import io.github.obrenoxs.simuladev.task.dto.response.TaskResponse;
import io.github.obrenoxs.simuladev.task.repository.TaskRepository;
import io.github.obrenoxs.simuladev.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    private final CompanyLinkRepository companyLinkRepository;
    private final TaskEngine taskEngine;
    private final TaskRepository taskRepository;
    private final CompanyLinkService companyLinkService;

    public TaskService(CompanyLinkRepository companyLinkRepository, TaskEngine taskEngine, TaskRepository taskRepository, CompanyLinkService companyLinkService) {
        this.companyLinkRepository = companyLinkRepository;
        this.taskEngine = taskEngine;
        this.taskRepository = taskRepository;
        this.companyLinkService = companyLinkService;
    }

    public TaskResponse create(UUID companyLinkId, User user) {
        CompanyLink companyLink = companyLinkRepository.findById(companyLinkId)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado"));


    }
}
