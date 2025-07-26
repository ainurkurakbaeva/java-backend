package com.taskflow.TaskFlow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import taskFlow.TaskflowApplication;
import taskFlow.model.TaskHistory;
import taskFlow.repository.TaskHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static taskFlow.enums.ActionType.CREATE;
import static taskFlow.enums.ActionType.UPDATE;
@ContextConfiguration(classes = TaskflowApplication.class)

@DataMongoTest
@ActiveProfiles("test")
class TaskHistoryRepositoryTest {

    @Autowired
    TaskHistoryRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }


    @Test
    void saveTaskHistory_successfullySaves() {
        TaskHistory history = new TaskHistory();
        history.setTaskId(1L);
        history.setAction(UPDATE);
        history.setTimestamp(LocalDateTime.now());

        TaskHistory saved = repository.save(history);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTaskId()).isEqualTo(1L);
        assertThat(saved.getAction()).isEqualTo(UPDATE);
    }

    @Test
    void findByTaskId_returnsCorrectHistory() {
        TaskHistory history = new TaskHistory();
        history.setTaskId(42L);
        history.setAction(CREATE);
        history.setTimestamp(LocalDateTime.now());
        repository.save(history);

        List<TaskHistory> result = repository.findByTaskId(42L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAction()).isEqualTo(CREATE);
    }
}
