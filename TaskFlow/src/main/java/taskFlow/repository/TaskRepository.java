package taskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskFlow.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
