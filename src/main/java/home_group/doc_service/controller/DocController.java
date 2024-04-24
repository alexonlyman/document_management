package home_group.doc_service.controller;


import home_group.doc_service.entity.Document;
import home_group.doc_service.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/doc_panel")
@RequiredArgsConstructor
public class DocController {
  private final DocumentService documentService;

    @PostMapping("/createDoc")
    public ResponseEntity<?> createDoc(@RequestBody Document document) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        documentService.createDocument(document);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/editDoc/{id}")
    public ResponseEntity<?> editDoc(@PathVariable Integer id,@RequestBody String content) {
        documentService.editDocument(id, content);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getDocs")
    public ResponseEntity<?> getListOfDocs() {
        return ResponseEntity.ok(documentService.getAllDocs());
    }
}
