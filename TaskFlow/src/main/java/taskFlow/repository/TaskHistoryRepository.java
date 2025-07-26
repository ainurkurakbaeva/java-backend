package taskFlow.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import taskFlow.model.TaskHistory;

import java.util.List;

public interface TaskHistoryRepository extends MongoRepository<TaskHistory, ObjectId> {
    List<TaskHistory> findByTaskId(Long taskId);
}
