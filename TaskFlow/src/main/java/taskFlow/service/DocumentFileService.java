package taskFlow.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import taskFlow.enums.ActionType;
import taskFlow.enums.LogLevel;
import taskFlow.model.DocumentFile;
import taskFlow.repository.DocumentFileRepository;
import taskFlow.service.LogEntryService;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DocumentFileService {
    private final GridFsTemplate gridFsTemplate;
    private final DocumentFileRepository documentFileRepository;
    private final LogEntryService logEntryService;

    public DocumentFileService(GridFsTemplate gridFsTemplate, DocumentFileRepository documentFileRepository, LogEntryService logEntryService){
        this.gridFsTemplate = gridFsTemplate;
        this.documentFileRepository = documentFileRepository;
        this.logEntryService = logEntryService;
    }
    public void upload(Long taskId, MultipartFile multipartFile) throws IOException{
        if(multipartFile.getSize() > 10*1024*1024)
            throw new IllegalArgumentException("file too large.");
        String contentType = multipartFile.getContentType();
        if(!List.of("application/pdf", "image/png", "image/jpeg").contains(contentType))
            throw new IllegalArgumentException("unsupported file format.");
        ObjectId fileId = gridFsTemplate.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), contentType);

        DocumentFile documentFile = new DocumentFile();
        documentFile.setId(fileId);
        documentFile.setTaskId(taskId);
        documentFile.setFilename(multipartFile.getOriginalFilename());
        documentFile.setFileType(contentType);
        documentFile.setUploadedAt(LocalDateTime.now());
        documentFile.setSize(multipartFile.getSize());

        documentFileRepository.save(documentFile);

        logEntryService.log(
                LogLevel.INFO,
                "Файл успешно загружен",
                Map.of("taskId", taskId, "filename", multipartFile.getOriginalFilename()),
                ActionType.CREATE
        );
    }
    public List<DocumentFile> getByTaskId(Long taskId){
        return documentFileRepository.findByTaskId(taskId);
    }
    public GridFsResource download(String id) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));
        if(gridFSFile == null) return null;

        return gridFsTemplate.getResource(gridFSFile);
    }
    public void delete(String id){
        ObjectId objectId = new ObjectId(id);
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(objectId)));
        documentFileRepository.deleteById(objectId);
    }
}
