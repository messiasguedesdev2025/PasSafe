package com.br.passafe.entities;

import jakarta.persistence.*;
import lombok.Data;

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

    private String nivelAcesso; // Ex: "ADMIN, "DEV!, VIEWER"

    @OneToMany(mappedBy = "owner")
    private List<Secret> secrets ;

}
