package taskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskFlow.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {}

