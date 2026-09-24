package io.github.obrenoxs.simuladev.task.controller;

import io.github.obrenoxs.simuladev.task.dto.response.TaskResponse;
import io.github.obrenoxs.simuladev.task.service.TaskService;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/company-links/{companyLinkId}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@PathVariable UUID companyLinkId, Authentication authentication) {
        Object object = authentication.getPrincipal();
        UUID id = UUID.fromString(object.toString());

        User user = userService.findByIdEntity(id);

        TaskResponse taskResponse = taskService.create(companyLinkId, user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(taskResponse.id()).toUri();

        return ResponseEntity.created(uri).body(taskResponse);
    }
}
