package taskFlow.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@Document(collection = "document_file")
    public class DocumentFile {
    @Id
    private ObjectId id;
    private Long taskId;
    private String filename;
    private String fileType;
    private LocalDateTime uploadedAt;
    private Long size;
}
