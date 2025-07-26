package taskFlow.DTO.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import taskFlow.enums.ProjectStatus;


@Getter
@Setter

public class ProjectRequest {

    @NotBlank(message = "Project name is required")
    private String name;
    @NotBlank(message = "Project description is required")
    private String description;
    private ProjectStatus status;
    private Long ownerId;
}
