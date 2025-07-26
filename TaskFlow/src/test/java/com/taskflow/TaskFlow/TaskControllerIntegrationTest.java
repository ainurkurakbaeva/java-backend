package com.taskflow.TaskFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import taskFlow.DTO.request.TaskRequest;
import taskFlow.TaskflowApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ContextConfiguration(classes = TaskflowApplication.class)

@SpringBootTest
@AutoConfigureMockMvc

public class TaskControllerIntegrationTest {
@Autowired
    private MockMvc mockMvc;
@Autowired
    private ObjectMapper objectMapper;
@Test
    void createTaskWithEmptyTitle_BadReq() throws Exception {
    TaskRequest taskRequest = new TaskRequest();
    taskRequest.setTitle("");
    taskRequest.setDescription("desc");
    mockMvc.perform(post("/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskRequest)))
            .andExpect(status().isBadRequest());
}
@Test
    void deleteTaskNotFount() throws Exception{
    mockMvc.perform(delete("/tasks/1111"))
            .andExpect(status().isNotFound());
}
}
