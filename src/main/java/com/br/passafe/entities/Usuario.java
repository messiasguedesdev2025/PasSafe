package com.br.passafe.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean isActivated = false; // Ativado ou não

    private String verificationCode; // O código que será enviado por email

    private LocalDateTime verificationCodeExpiresAt; // Validade do código

    @OneToMany(mappedBy = "owner")
    private List<Secret> secrets;
}
