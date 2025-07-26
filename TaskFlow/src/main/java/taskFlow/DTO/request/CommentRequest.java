package taskFlow.DTO.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class CommentRequest {
    @NotBlank(message = "content should be fill")
    private String content;
    private Long taskId;
    private Long userId;
}
