package home_group.doc_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import home_group.doc_service.dto.DocumentInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)

class DeserializeDocTest {
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    DeserializeDoc doc;

    @Test
    void deserialize() throws IOException {
        byte[] bytes = "{\"email\":\"test@example.com\",\"description\":\"Test document\",\"dateTime\":\"2024-04-23T10:15:30\"}".getBytes();
        DocumentInfo documentInfo = new DocumentInfo("test@example.com","Test document",LocalDateTime.parse("2024-04-23T10:15:30"));
        when(objectMapper.readValue(bytes, DocumentInfo.class)).thenReturn(documentInfo);
        DocumentInfo documentInf = doc.deserialize("topic", bytes);
        assertEquals(documentInfo, documentInf);
    }
}