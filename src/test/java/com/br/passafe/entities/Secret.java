package com.br.passafe.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "secrets")
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario owner;
}
