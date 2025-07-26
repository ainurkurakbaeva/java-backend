package taskFlow.DTO.response;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
