package home_group.doc_service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@JsonDeserialize
public class DocumentInfo {
    private String email;
    private String description;
    private LocalDateTime dateTime;

    @JsonCreator
    public DocumentInfo(@JsonProperty("email") String email,
                        @JsonProperty("description") String description,
                        @JsonProperty("dateTime") LocalDateTime dateTime) {

        this.email = email;
        this.description = description;
        this.dateTime = dateTime;
    }
}
