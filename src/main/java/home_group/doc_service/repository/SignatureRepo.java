package home_group.doc_service.repository;

import home_group.doc_service.entity.DocSignature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignatureRepo extends JpaRepository<DocSignature, Integer> {

}
