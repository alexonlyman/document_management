package home_group.doc_service.service;

import ch.qos.logback.classic.Logger;
import home_group.doc_service.dto.DocumentDto;
import home_group.doc_service.dto.DocumentInfo;
import home_group.doc_service.dto.SignatureDto;
import home_group.doc_service.entity.DocSignature;
import home_group.doc_service.entity.Document;
import home_group.doc_service.entity.User;
import home_group.doc_service.exeptions.DescriptionIsEmpty;
import home_group.doc_service.mapper.Mapper;
import home_group.doc_service.repository.DocumentRepo;
import home_group.doc_service.repository.SignatureRepo;
import home_group.doc_service.repository.UserRepo;
import home_group.doc_service.utils.KeyLoader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilder;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepo documentRepo;
    Logger logger = (Logger) LoggerFactory.getLogger(AuthService.class);
    private final Mapper mapper;
    private final SignatureRepo signatureRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private final KafkaTemplate<String,DocumentInfo> kafkaTemplate;


    public Document createDocument(Document document) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (document.getDescription().isEmpty() && document.getDescription().isBlank()) {
            throw new DescriptionIsEmpty("description is empty");
        }
        DocumentDto doc = DocumentDto.builder()
                .content(document.getContent())
                .docSignature(createSignature(document))
                .description(document.getDescription())
                .user(findUserbyEmail())
                .build();

        Document mappingDoc = mapper.mapTo(doc, Document.class);
        logger.info("document data " + doc);
        Document docEntity = documentRepo.save(mappingDoc);
        DocumentInfo info = documentInfo(document);
        kafkaTemplate.send("document", info);
        return docEntity;


    }

    public void editDocument(Integer id, String content) {
        Document doc = documentRepo.findDocumentById(id);
        logger.info("doc = " + doc);
        doc.setContent(content);
        logger.info("content = " + content);
        documentRepo.save(doc);
    }

    public List<Integer> getAllDocs() {
        List<Document> allDocs = documentRepo.findAll();
        List<Integer> allDocsId = new ArrayList<>();
        for (Document document : allDocs) {
            allDocsId.add(document.getId());
        }
        return allDocsId;
    }

    private DocSignature createSignature(Document document) throws
            IOException,
            NoSuchAlgorithmException,
            InvalidKeySpecException {
        PrivateKey privateKey = KeyLoader.loadPrivateKey();
        SignatureDto signatureDto = SignatureDto.builder()
                .document(document)
                .user(findUserbyEmail())
                .privateKey(privateKey)
                .build();
        DocSignature signature = mapper.mapTo(signatureDto, DocSignature.class);
        return signatureRepo.save(signature);
    }
    private void validateSign (Integer docId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Document document = documentRepo.findDocumentById(docId);
        PrivateKey keyLoader = KeyLoader.loadPrivateKey();
        if (document.getDocSignature().getPrivateKey().equals(keyLoader)) {
            System.out.println("doc is valid");
        } else {
            System.out.println("doc is invalid");
        }
    }

    private DocumentInfo documentInfo(Document document) {
        DocumentInfo documentInfo = new DocumentInfo();
        documentInfo.setDescription(document.getDescription());
        documentInfo.setEmail(document.getUser().getEmail());
        documentInfo.setDateTime(LocalDateTime.now());
        return documentInfo;
    }

    private User findUserbyEmail() {
        String userStr = userService.getUser();
        return userRepo.findUserByEmail(userStr);
    }

    }


