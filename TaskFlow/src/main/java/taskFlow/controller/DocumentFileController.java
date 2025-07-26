package taskFlow.controller;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import taskFlow.model.DocumentFile;
import taskFlow.service.DocumentFileService;

import java.io.IOException;
import java.util.List;

@RestController
public class DocumentFileController {

    private final DocumentFileService documentFileService;

    public DocumentFileController(DocumentFileService documentFileService) {
        this.documentFileService = documentFileService;
    }

    @PostMapping("/tasks/{taskId}/documents")
    public ResponseEntity<String> upload(@PathVariable("taskId") Long taskId,
                                         @RequestParam("file") MultipartFile multipartFile) {
        try {
            documentFileService.upload(taskId, multipartFile);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/tasks/{taskId}/documents")
    public ResponseEntity<List<DocumentFile>> list(@PathVariable("taskId") Long taskId) {
        return ResponseEntity.ok(documentFileService.getByTaskId(taskId));
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<?> download(@PathVariable String id) {
        GridFsResource gridFsResource = documentFileService.download(id);
        if (gridFsResource == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + gridFsResource.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(gridFsResource.getContentType()))
                .body(gridFsResource);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        documentFileService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}
