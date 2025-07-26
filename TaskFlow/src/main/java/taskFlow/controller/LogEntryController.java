package taskFlow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import taskFlow.enums.LogLevel;
import taskFlow.model.LogEntry;
import taskFlow.service.LogEntryService;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogEntryController {
    private LogEntryService logEntryService;
    public LogEntryController(LogEntryService logEntryService){
        this.logEntryService = logEntryService;
    }

    @GetMapping
    public ResponseEntity<List<LogEntry>> getLogs(@RequestParam(required = false) LogLevel level) {
        if (level == null) {
            return ResponseEntity.ok(logEntryService.getLogEntry());
        } else {
            return ResponseEntity.ok(logEntryService.getByLevel(level));
        }
    }
}

/**
 *
 * [
 *   {
 *     "id": {
 *       "timestamp": 1752585961,
 *       "date": "2025-07-15T13:26:01.000+00:00"
 *     },
 *     "level": "INFO",
 *     "message": "Файл успешно загружен",
 *     "timestamp": "2025-07-15T18:26:01.408",
 *     "context": {
 *       "taskId": 2,
 *       "filename": "Куракбаева Айнур Бахтғалийқызы.pdf"
 *     },
 *     "action": "CREATE"
 *   }
 * ]
 * */
