package taskFlow.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import taskFlow.enums.ActionType;
import taskFlow.enums.LogLevel;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document(collection = "log_entries")

public class LogEntry {
    @Id
    private ObjectId id;
    private LogLevel level;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, Object> context;
    private ActionType action;
}
