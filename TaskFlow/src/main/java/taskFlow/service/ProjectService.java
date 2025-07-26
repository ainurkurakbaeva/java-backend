package taskFlow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import taskFlow.DTO.request.ProjectRequest;
import taskFlow.DTO.response.ProjectResponse;
import taskFlow.enums.ProjectStatus;
import taskFlow.model.Project;
import taskFlow.model.User;
import taskFlow.repository.ProjectRepository;
import taskFlow.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        project.setOwner(owner);

        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());

        Project saved = projectRepository.save(project);
        return mapToResponse(saved);
    }

    public ProjectResponse getById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
        return mapToResponse(project);
    }

    public Page<ProjectResponse> getAll(Pageable pageable) {
        return projectRepository.findAll(pageable).map(this::mapToResponse);
    }

    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        project.setOwner(owner);

        project.setUpdatedAt(LocalDateTime.now());

        Project updated = projectRepository.save(project);
        return mapToResponse(updated);
    }

    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new NoSuchElementException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    private ProjectResponse mapToResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStatus(project.getStatus());
        response.setOwnerId(project.getOwner().getId());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return response;
    }
    public void complete(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        project.setStatus(ProjectStatus.COMPLETED);
        projectRepository.save(project);
    }

}
