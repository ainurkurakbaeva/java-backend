package taskFlow.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import taskFlow.enums.ActionType;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "taskHistory")

public class TaskHistory {
    @Id
    private ObjectId id;
    private Long taskId;
    private ActionType action;
    private Long performedBy;
    private LocalDateTime timestamp;
    private Map<String, Object> details;
}
