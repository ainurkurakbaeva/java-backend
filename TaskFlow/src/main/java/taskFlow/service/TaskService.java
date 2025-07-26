package taskFlow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import taskFlow.DTO.request.TaskRequest;
import taskFlow.DTO.response.TaskResponse;
import taskFlow.enums.ActionType;
import taskFlow.model.Task;
import taskFlow.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskHistoryService taskHistoryService;

    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDeadline(request.getDeadline());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        Task saved = taskRepository.save(task);

        taskHistoryService.logAction(
                saved.getId(),
                0L,
                ActionType.CREATE,
                Map.of("title", task.getTitle())
        );

        return mapToResponse(saved);

    }

    public Page<TaskResponse> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable).map(this::mapToResponse);
    }

    public TaskResponse findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        return mapToResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDeadline(request.getDeadline());
        task.setUpdatedAt(LocalDateTime.now());

        Task updated = taskRepository.save(task);
        taskHistoryService.logAction(
                updated.getId(),
                updated.getAssignedUser() != null ? updated.getAssignedUser().getId() : 0L,
                ActionType.UPDATE,
                Map.of(
                        "title", updated.getTitle(),
                        "description", updated.getDescription(),
                        "status", updated.getStatus().toString(),
                        "priority", updated.getPriority().toString()
                )
        );
        return mapToResponse(updated);

    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }


    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDeadline(task.getDeadline());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        return response;
    }

}
