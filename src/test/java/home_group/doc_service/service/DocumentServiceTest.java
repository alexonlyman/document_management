package home_group.doc_service.service;

import home_group.doc_service.dto.DocumentDto;
import home_group.doc_service.dto.DocumentInfo;
import home_group.doc_service.dto.Role;
import home_group.doc_service.dto.SignatureDto;
import home_group.doc_service.entity.DocSignature;
import home_group.doc_service.entity.Document;
import home_group.doc_service.entity.User;
import home_group.doc_service.mapper.Mapper;
import home_group.doc_service.repository.DocumentRepo;
import home_group.doc_service.repository.SignatureRepo;
import home_group.doc_service.repository.UserRepo;
import home_group.doc_service.utils.KeyLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(KeyLoader.class)
@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    @InjectMocks
    DocumentService documentService;
    @Mock
    DocSignature docSignature;
    @Mock
    User user;
    @Mock
    Mapper mapper;
    @Mock
    DocumentRepo documentRepo;
    @Mock
    SignatureRepo signatureRepo;
    @Mock
    UserService userService;
    @Mock
    PrivateKey privateKey;
    @Mock
    UserRepo userRepo;
    @Mock
    DocumentInfo documentInfo;
    @Mock
    KafkaTemplate<String, DocumentInfo> kafkaTemplate;


    @Test
    void createDocument() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        when(userService.getUser()).thenReturn("testemail");
        when(userRepo.findUserByEmail(userService.getUser())).thenReturn(user);
        when(documentInfo.getDateTime()).thenReturn(LocalDateTime.now().withSecond(0).withNano(0));
        when(documentInfo.getEmail()).thenReturn("testemail");
        when(documentInfo.getDescription()).thenReturn("testdescr");

        Document document = mock(Document.class);
        privateKey = KeyLoader.loadPrivateKey();


        when(document.getContent()).thenReturn("testcontent");
        when(document.getDescription()).thenReturn("testdescr");

        when(mapper.mapTo(any(SignatureDto.class), eq(DocSignature.class))).thenReturn(docSignature);
        when(mapper.mapTo(any(DocumentDto.class), eq(Document.class))).thenReturn(document);
        when(documentRepo.save(any(Document.class))).thenReturn(document);

        Document doc = documentService.createDocument(document);
        System.out.println("document "+ doc);

        Assertions.assertNotNull(doc);
        Assertions.assertNotNull(privateKey);

        Assertions.assertEquals("testcontent",doc.getContent());
        Assertions.assertEquals("testdescr",doc.getDescription());
        Assertions.assertEquals(LocalDateTime.now().withSecond(0).withNano(0), documentInfo.getDateTime());
        Assertions.assertEquals("testemail", documentInfo.getEmail());
        Assertions.assertEquals("testdescr", documentInfo.getDescription());

        verify(documentRepo, times(1)).save(doc);
        verify(documentInfo, times(1)).getDateTime();
        verify(documentInfo, times(1)).getEmail();
        verify(documentInfo, times(1)).getDescription();


    }



    @Test
    void editDocument() {
        Document document = new Document();
        document.setId(1);
        document.setContent("testConte");
        when(documentRepo.findDocumentById(1)).thenReturn(document);
        documentService.editDocument(1, "newContent");
        verify(documentRepo).findDocumentById(1);
        verify(documentRepo).save(document);
        Assertions.assertEquals("newContent", document.getContent());

    }

    @Test
    void getAllDocs() {
        Document doc1 = new Document();
        Document doc2 = new Document();
        doc1.setId(1);
        doc2.setId(2);
        when(documentRepo.findAll()).thenReturn(Arrays.asList(doc1, doc2));
        List<Integer> result = documentService.getAllDocs();
        Assertions.assertEquals(Arrays.asList(1, 2), result);

    }
}