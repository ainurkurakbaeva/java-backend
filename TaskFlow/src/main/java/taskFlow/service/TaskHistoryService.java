    package taskFlow.service;

    import jakarta.websocket.OnClose;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import taskFlow.enums.ActionType;
    import taskFlow.model.Task;
    import taskFlow.model.TaskHistory;
    import taskFlow.repository.TaskHistoryRepository;

    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Map;
    @Service

    public class TaskHistoryService {
        private final TaskHistoryRepository historyRepository;

        public TaskHistoryService(TaskHistoryRepository taskHistoryRepository){
            this.historyRepository = taskHistoryRepository;
        }
        public void logAction(Long taskId,Long performedBy, ActionType actionType, Map<String, Object> details){
            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setTaskId(taskId);
            taskHistory.setAction(actionType);
            taskHistory.setTimestamp(LocalDateTime.now());
            taskHistory.setPerformedBy(performedBy);
            taskHistory.setDetails(details);

            historyRepository.save(taskHistory);


        }

        public List<TaskHistory> getHistory(Long taskId){
            return historyRepository.findByTaskId(taskId);
        }
    }
/**
 * [
 *   {
 *     "id": {
 *       "timestamp": 1752642282,
 *       "date": "2025-07-16T05:04:42.000+00:00"
 *     },
 *     "taskId": 1,
 *     "action": "UPDATE",
 *     "performedBy": 0,
 *     "timestamp": "2025-07-16T10:04:42.526",
 *     "details": {
 *       "description": "Refactor models and repos",
 *       "priority": "MEDIUM",
 *       "title": "Updated backend setup",
 *       "status": "IN_PROGRESS"
 *     }
 *   }
 * ]
 * */