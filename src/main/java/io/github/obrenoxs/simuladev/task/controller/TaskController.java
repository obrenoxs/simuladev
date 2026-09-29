package io.github.obrenoxs.simuladev.task.controller;

import io.github.obrenoxs.simuladev.task.dto.response.TaskResponse;
import io.github.obrenoxs.simuladev.task.service.TaskService;
import io.github.obrenoxs.simuladev.user.entity.User;
import io.github.obrenoxs.simuladev.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping(value = "/company-links/{companyLinkId}")
    public ResponseEntity<TaskResponse> create(@PathVariable UUID companyLinkId, Authentication authentication) {
        User user = extractUser(authentication);

        TaskResponse taskResponse = taskService.create(companyLinkId, user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(taskResponse.id()).toUri();

        return ResponseEntity.created(uri).body(taskResponse);
    }

    @PatchMapping(value = "/{taskId}")
    public ResponseEntity<Void> deliver(@PathVariable UUID taskId, Authentication authentication) {
        User user = extractUser(authentication);

        taskService.deliver(taskId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/company-links/{companyLinkId}")
    public ResponseEntity<List<TaskResponse>> findAll(@PathVariable UUID companyLinkId, Authentication authentication) {
        User user = extractUser(authentication);

        List<TaskResponse> taskList = taskService.findAllTasks(companyLinkId, user);

        return ResponseEntity.ok().body(taskList);
    }

    private User extractUser(Authentication authentication) {
        Object object = authentication.getPrincipal();
        UUID id = UUID.fromString(object.toString());

        return userService.findByIdEntity(id);
    }
}
