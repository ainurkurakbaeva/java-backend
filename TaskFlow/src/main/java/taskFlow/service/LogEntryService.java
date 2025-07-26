package taskFlow.service;

import org.springframework.stereotype.Service;
import taskFlow.enums.ActionType;
import taskFlow.enums.LogLevel;
import taskFlow.model.LogEntry;
import taskFlow.repository.LogEntryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
public class LogEntryService {
    private LogEntryRepository logEntryRepository;


    public LogEntryService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }


    public void log(LogLevel level, String message, Map<String,Object> context, ActionType actionType){
        LogEntry logEntry = new LogEntry();
        logEntry.setLevel(level);
        logEntry.setMessage(message);
        logEntry.setContext(context);
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setAction(actionType);

        logEntryRepository.save(logEntry);
    }
    public List<LogEntry> getLogEntry(){
        return logEntryRepository.findAll();
    }
    public List<LogEntry> getByLevel(LogLevel level){
        return logEntryRepository.findByLevel(level);
    }
}
