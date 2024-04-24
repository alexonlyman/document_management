package home_group.doc_service.repository;

import home_group.doc_service.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<Document, Integer> {
    Document findDocumentById (Integer id);

}
