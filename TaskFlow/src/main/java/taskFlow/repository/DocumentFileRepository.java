package taskFlow.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import taskFlow.model.DocumentFile;
import java.util.List;

public interface DocumentFileRepository extends MongoRepository<DocumentFile, ObjectId> {
    List<DocumentFile> findByTaskId(Long taskId);
}
