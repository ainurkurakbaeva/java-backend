package taskFlow.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import taskFlow.enums.Status;
import taskFlow.enums.TaskPriority;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull
    private Status status;

    @NotNull
    private TaskPriority priority;

    private LocalDateTime deadline;

    private Long assignedUserId;

    private Long projectId;
}
