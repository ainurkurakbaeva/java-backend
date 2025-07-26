package taskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskFlow.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {}
