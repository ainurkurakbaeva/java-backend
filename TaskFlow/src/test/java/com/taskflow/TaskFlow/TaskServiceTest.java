package com.taskflow.TaskFlow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import taskFlow.DTO.request.TaskRequest;
import taskFlow.DTO.response.TaskResponse;
import taskFlow.TaskflowApplication;
import taskFlow.enums.ActionType;
import taskFlow.model.Task;
import taskFlow.repository.TaskRepository;
import taskFlow.service.TaskHistoryService;
import taskFlow.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static taskFlow.enums.Status.IN_PROGRESS;
import static taskFlow.enums.TaskPriority.MEDIUM;
@ContextConfiguration(classes = TaskflowApplication.class)

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskHistoryService taskHistoryService;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private TaskRequest createRequest() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("Test Task for TaskService");
        taskRequest.setDescription("mock test");
        taskRequest.setStatus(IN_PROGRESS);
        taskRequest.setPriority(MEDIUM);
        taskRequest.setDeadline(LocalDateTime.now().plusDays(2));
        return taskRequest;
    }

    private Task createTaskFromRequest(TaskRequest taskRequest) {
        Task task = new Task();
        task.setId(7L);
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());
        task.setPriority(taskRequest.getPriority());
        task.setDeadline(taskRequest.getDeadline());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return task;
    }

    @Test
    void createTaskTest() {
        TaskRequest taskRequest = createRequest();
        Task taskToSave = createTaskFromRequest(taskRequest);

        when(taskRepository.save(any(Task.class))).thenReturn(taskToSave);
        TaskResponse taskResponse = taskService.createTask(taskRequest);
        assertEquals(taskRequest.getTitle(), taskResponse.getTitle(), "same");
        assertEquals(taskRequest.getStatus(), taskResponse.getStatus(), "same status");
        verify(taskRepository).save(any(Task.class));
        verify(taskHistoryService).logAction(
                eq(taskToSave.getId()), eq(0l), eq(ActionType.CREATE), anyMap()
        );
    }

    @Test
    void testFindByIdSuccess() {
        Task task = createTaskFromRequest(createRequest());
        when(taskRepository.findById(7L)).thenReturn(Optional.of(task));
        TaskResponse taskResponse = taskService.findById(7L);
        assertEquals(taskResponse.getId(), task.getId());
        verify(taskRepository).findById(7L);
    }
    @Test
    void testFindByIdNotFound(){
        when(taskRepository.findById(111L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskService.findById(111L), "id не существует");
    }
    @Test
    void testUpdateTask(){
        Task existingTask = createTaskFromRequest(createRequest());
        TaskRequest updatedRequest = createRequest();
        updatedRequest.setTitle("New Title (updated)");
        when(taskRepository.findById(7L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));
        TaskResponse taskResponse = taskService.updateTask(7L, updatedRequest);

        assertEquals("New Title (updated)", taskResponse.getTitle());
        verify(taskHistoryService).logAction(eq(7L), eq(0L), eq(ActionType.UPDATE), anyMap());
    }
    @Test
    void testUpdatedTaskNotFound(){
        when(taskRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskService.updateTask(7L, createRequest()));
        System.out.println("updateTask test passed"); // для проверки for me

    }
    @Test
    void testFindAll(){
        Task task = createTaskFromRequest(createRequest());
        Page<Task> page = new PageImpl<>(List.of(task));
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<TaskResponse> result = taskService.findAll(PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
        assertEquals(task.getTitle(), result.getContent().get(0).getTitle());
    }
    @Test
    void testDeleteTask() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);
        taskService.deleteTask(taskId);
        verify(taskRepository).existsById(taskId);
        verify(taskRepository).deleteById(taskId);
        System.out.println("deleted successfully");
    }

}