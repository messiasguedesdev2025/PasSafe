package com.br.passafe.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "secrets")
@EntityListeners(AuditingEntityListener.class)
@Data
public class Secret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String siteUrl;
    private String usernameInSite;

    @Column(columnDefinition = "TEXT")
    private String encryptedTotpKey; // Chave secreta A2F (TOTP)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String encryptedPassword; // Senha criptografada do site

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt; // Quando foi criado

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt; // Última modificação

    private LocalDateTime lastAutoFillAt; // Último preenchimento automático

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario owner;

    @OneToMany(mappedBy = "secret", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<PasswordHistory> passwordHistory; // Histórico das últimas senhas
}
