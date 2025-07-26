package taskFlow.DTO.response;

import lombok.Data;
import org.springframework.cglib.core.Local;
import taskFlow.enums.ProjectStatus;
import taskFlow.model.User;
import java.time.LocalDateTime;

@Data
public class ProjectResponse{
      private Long id;

      private String name;

      private String description;

      private ProjectStatus status;

      private Long ownerId;

      private LocalDateTime createdAt;

      private LocalDateTime updatedAt;

}
