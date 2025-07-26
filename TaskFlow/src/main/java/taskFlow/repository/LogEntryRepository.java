package taskFlow.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import taskFlow.enums.LogLevel;
import taskFlow.model.LogEntry;

import java.util.List;

public interface LogEntryRepository extends MongoRepository<LogEntry, ObjectId> {
    List<LogEntry> findByLevel(LogLevel level);
}
