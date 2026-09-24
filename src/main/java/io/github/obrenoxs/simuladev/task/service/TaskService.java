package io.github.obrenoxs.simuladev.task.service;

import io.github.obrenoxs.simuladev.companylink.repository.CompanyLinkRepository;
import io.github.obrenoxs.simuladev.engine.service.TaskEngine;
import io.github.obrenoxs.simuladev.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final CompanyLinkRepository companyLinkRepository;
    private final TaskEngine taskEngine;
    private final TaskRepository taskRepository;

    public TaskService(CompanyLinkRepository companyLinkRepository, TaskEngine taskEngine, TaskRepository taskRepository) {
        this.companyLinkRepository = companyLinkRepository;
        this.taskEngine = taskEngine;
        this.taskRepository = taskRepository;
    }
}
