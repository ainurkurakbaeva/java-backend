package com.taskflow.TaskFlow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import taskFlow.DTO.request.ProjectRequest;
import taskFlow.DTO.response.ProjectResponse;
import taskFlow.TaskflowApplication;
import taskFlow.enums.ProjectStatus;
import taskFlow.model.Project;
import taskFlow.model.User;
import taskFlow.repository.ProjectRepository;
import taskFlow.repository.UserRepository;
import taskFlow.service.ProjectService;


import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static taskFlow.enums.ProjectStatus.ACTIVE;
@ContextConfiguration(classes = TaskflowApplication.class)
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ProjectRequest createRequest(){
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setName("Test Project");
        projectRequest.setDescription("integration testing");
        projectRequest.setStatus(ACTIVE);
        projectRequest.setOwnerId(7L);
        return projectRequest;
    }
    private Project createProject(ProjectRequest projectRequest){
        Project project = new Project();
        project.setId(7L);
        project.setDescription(projectRequest.getDescription());
        project.setName(projectRequest.getName());
        project.setUpdatedAt(LocalDateTime.now());
        project.setCreatedAt(LocalDateTime.now());
        project.setStatus(projectRequest.getStatus());
        User user = new User();
        user.setId(projectRequest.getOwnerId());
        project.setOwner(user);

        return project;
    }
    @Test
    void testCreateProjectSuccess(){
        ProjectRequest request = createRequest();
        Project project = createProject(request);

        when(userRepository.findById(request.getOwnerId())).thenReturn(Optional.of(project.getOwner()));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse response = projectService.create(request);

        assertEquals(request.getName(), response.getName());
        verify(projectRepository).save(any(Project.class));
        verify(userRepository).findById(request.getOwnerId());

    }
    @Test
    void testCreateProjectUserNotFound() {
        ProjectRequest request = createRequest();
        when(userRepository.findById(request.getOwnerId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> projectService.create(request));
    }
    @Test
    void testCompleteProjectSuccess() {
        Project project = new Project();
        project.setId(7L);
        project.setName("Test Project");
        project.setStatus(ProjectStatus.ACTIVE);

        when(projectRepository.findById(7L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));
        projectService.complete(7L);
        assertEquals(ProjectStatus.COMPLETED, project.getStatus());
        verify(projectRepository).save(project);
    }
}
//Проверить создание и завершение проекта.