package home_group.doc_service.dto;

import home_group.doc_service.entity.Document;
import home_group.doc_service.entity.User;
import lombok.Builder;
import lombok.Data;

import java.security.PrivateKey;

@Data
@Builder
public class SignatureDto {
    private PrivateKey privateKey;
    private Document document;
    private User user;

}
