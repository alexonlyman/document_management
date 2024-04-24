package home_group.doc_service.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.security.PrivateKey;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "doc_signature")
public class DocSignature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private PrivateKey privateKey;
    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}

