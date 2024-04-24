package home_group.doc_service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final ObjectMapper objectMapper;

    public <S, T> T mapTo(S source, Class<T> target) {
        try {
           String jsonString = objectMapper.writeValueAsString(source);
            return objectMapper.readValue(jsonString, target);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("wrong mapping json " + e.getMessage());
        }
    }
}
