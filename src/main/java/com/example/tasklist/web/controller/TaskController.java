package com.example.tasklist.web.controller;

import com.example.tasklist.domain.task.Task;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.web.dto.task.TaskDto;
import com.example.tasklist.web.dto.validation.OnUpdate;
import com.example.tasklist.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PutMapping
    @Operation(summary = "Update task")
//    @PreAuthorize("canAccessTask(#dto.id)")   // @EnableMethodSecurity почему-то не работает с CustomSecurityExceptionHandler
//    лучше использовать CustomSecurityExpression, как в UserController
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get TaskDto by id")
//    @PreAuthorize("canAccessTask(#id)") // @EnableMethodSecurity почему-то не работает с CustomSecurityExceptionHandler
//    лучше использовать CustomSecurityExpression, как в UserController
    public TaskDto getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
//    @PreAuthorize("canAccessTask(#id)") // @EnableMethodSecurity почему-то не работает с CustomSecurityExceptionHandler
//    лучше использовать CustomSecurityExpression, как в UserController
    public void deleteById(@PathVariable Long id) {
        taskService.delete(id);
    }
}
