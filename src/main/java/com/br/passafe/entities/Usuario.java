package com.br.passafe.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; // NOVO: Nome de Usuário

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean activated;
    private String verificationCode;
    private LocalDateTime verificationCodeExpiresAt;

    public String email() {
        return email;
    }
}
