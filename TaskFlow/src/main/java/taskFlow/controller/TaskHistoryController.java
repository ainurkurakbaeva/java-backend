package taskFlow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taskFlow.model.TaskHistory;
import taskFlow.service.TaskHistoryService;

import java.util.List;

@RestController
@RequestMapping("/tasks/{taskId}/history")
public class TaskHistoryController {
    private TaskHistoryService taskHistoryService;
    public TaskHistoryController(TaskHistoryService taskHistoryService){
        this.taskHistoryService = taskHistoryService;
    }


@GetMapping
    public ResponseEntity<List<TaskHistory>> getHistoryList(@PathVariable Long taskId){
        return ResponseEntity.ok(taskHistoryService.getHistory(taskId));
}
}
