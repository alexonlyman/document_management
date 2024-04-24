package home_group.doc_service.dto;

import home_group.doc_service.entity.DocSignature;
import home_group.doc_service.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentDto {
    private String content;
    private String description;
    private DocSignature docSignature;
    private User user;
}
