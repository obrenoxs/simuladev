package io.github.obrenoxs.simuladev.task.service;

import io.github.obrenoxs.simuladev.companylink.entity.CompanyLink;
import io.github.obrenoxs.simuladev.companylink.service.CompanyLinkService;
import io.github.obrenoxs.simuladev.engine.result.EngineResult;
import io.github.obrenoxs.simuladev.engine.service.TaskEngine;
import io.github.obrenoxs.simuladev.shared.exception.ResourceNotFoundException;
import io.github.obrenoxs.simuladev.task.dto.response.TaskResponse;
import io.github.obrenoxs.simuladev.task.entity.Task;
import io.github.obrenoxs.simuladev.task.enums.TaskStatus;
import io.github.obrenoxs.simuladev.task.repository.TaskRepository;
import io.github.obrenoxs.simuladev.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskEngine taskEngine;
    private final CompanyLinkService companyLinkService;

    public TaskService(TaskRepository taskRepository, TaskEngine taskEngine, CompanyLinkService companyLinkService) {
        this.taskRepository = taskRepository;
        this.taskEngine = taskEngine;
        this.companyLinkService = companyLinkService;
    }

    public TaskResponse create(UUID companyLinkId, User user) {
        CompanyLink companyLink = companyLinkService.findEntityById(companyLinkId, user.getId());

        EngineResult result = taskEngine.nextTask(user, companyLink);

        Task task = new Task();
        task.setCreatedAt(LocalDateTime.now());
        task.setCompanyLink(companyLink);
        task.setStatus(TaskStatus.PENDING);
        task.setDeliveredAt(null);
        task.setConcept(result.concept());
        task.setDifficulty(result.difficulty());
        task.setType(result.type());
        task.setTicketText("Ticket a ser gerado pela IA (pendente)");

        task = taskRepository.save(task);

        return new TaskResponse(task.getId(),
                task.getConcept().getConceptName(),
                task.getType(), task.getDifficulty(),
                task.getTicketText(), task.getStatus(),
                task.getCreatedAt());
    }

    public void deliver(UUID taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));

        CompanyLink companyLink = task.getCompanyLink();

        UUID userCompanyLinkId = companyLink.getUser().getId();

        if (!userCompanyLinkId.equals(user.getId())) {
            throw new ResourceNotFoundException("Vínculo não encontrado");
        }

        task.setStatus(TaskStatus.DELIVERED);
        task.setDeliveredAt(LocalDateTime.now());

    }
}
