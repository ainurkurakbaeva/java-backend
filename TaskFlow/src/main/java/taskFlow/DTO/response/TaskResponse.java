package taskFlow.DTO.response;

import lombok.Data;
import taskFlow.enums.Status;
import taskFlow.enums.TaskPriority;

import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private TaskPriority priority;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long assignedUserId;
    private Long projectId;
}
