package taskFlow.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import taskFlow.DTO.request.CommentRequest;
import taskFlow.DTO.response.CommentResponse;
import taskFlow.service.CommentService;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@RequestBody CommentRequest request) {
        return commentService.create(request);
    }

    @GetMapping
    public Page<CommentResponse> getAll(Pageable pageable) {
        return commentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public CommentResponse getById(@PathVariable Long id) {
        return commentService.findById(id);
    }

    @PutMapping("/{id}")
    public CommentResponse update(@PathVariable Long id, @RequestBody CommentRequest request) {
        return commentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }

}
