package com.br.passafe.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_history")
@EntityListeners(AuditingEntityListener.class)
@Data
public class PasswordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String encryptedPassword; // A senha antiga (já criptografada)

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt; // Quando essa senha foi "substituída"

    @ManyToOne
    @JoinColumn(name = "secret_id")
    private Secret secret; // A qual registro de senha isso pertence
}
