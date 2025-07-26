package taskFlow.DTO.response;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class CommentResponse{

      private Long id;
      private String content;
      private Long taskId;
      private Long userId;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;
 }