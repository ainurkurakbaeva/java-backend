package com.taskflow.TaskFlow;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import taskFlow.TaskflowApplication;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
@ContextConfiguration(classes = TaskflowApplication.class)
@DataMongoTest
@ActiveProfiles("test")
class GridFsServiceTest {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Test
    void shouldUploadFileToGridFs() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "example.txt", "text/plain", "Hello GridFS".getBytes());
        DBObject metadata = new BasicDBObject();
        metadata.put("taskId", 123L);
        ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(),
                file.getContentType(), metadata);
        assertThat(fileId).isNotNull();
    }
    @Test
    void shouldDeleteFileFromGridFs() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file", "delete-me.txt", "text/plain", "Delete me".getBytes());
        ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
        GridFsResource resource = gridFsTemplate.getResource(fileId.toHexString());
        assertThat(resource.exists()).isFalse();
    }
}
