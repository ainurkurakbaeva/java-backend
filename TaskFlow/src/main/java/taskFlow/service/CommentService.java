package taskFlow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import taskFlow.DTO.request.CommentRequest;
import taskFlow.DTO.response.CommentResponse;
import taskFlow.model.Comment;
import taskFlow.model.Task;
import taskFlow.model.User;
import taskFlow.repository.CommentRepository;
import taskFlow.repository.TaskRepository;
import taskFlow.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentResponse create(CommentRequest request) {
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new NoSuchElementException("Task not found: " + request.getTaskId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + request.getUserId()));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setOwner(user);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return mapToResponse(saved);
    }

    public Page<CommentResponse> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable).map(this::mapToResponse);
    }

    public CommentResponse findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found: " + id));
        return mapToResponse(comment);
    }

    public CommentResponse update(Long id, CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comment not found: " + id));

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setOwner(user);
        comment.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(commentRepository.save(comment));
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    private CommentResponse mapToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setTaskId(comment.getTask().getId());
        response.setUserId(comment.getOwner().getId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        return response;
    }

}
