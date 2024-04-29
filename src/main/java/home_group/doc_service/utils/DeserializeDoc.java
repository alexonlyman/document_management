package home_group.doc_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import home_group.doc_service.dto.DocumentInfo;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
@RequiredArgsConstructor
@Configuration
public class DeserializeDoc implements Deserializer<DocumentInfo> {
   private final ObjectMapper mapper;

    @Override
    public DocumentInfo deserialize(String s, byte[] bytes) {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try {
            return mapper.readValue(bytes, DocumentInfo.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
