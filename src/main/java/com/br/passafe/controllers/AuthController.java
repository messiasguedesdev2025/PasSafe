package com.br.passafe.controllers;

import com.br.passafe.dtos.UsuarioRequestDTO;
import com.br.passafe.dtos.VerifyRequestDTO;
import com.br.passafe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite que a extensão ou app acessem sem bloqueios de CORS
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para Cadastro de Usuário
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UsuarioRequestDTO request) {
        authService.register(request);
        return ResponseEntity.ok("Usuário cadastrado com sucesso! Verifique seu e-mail para o código de ativação.");
    }

    /**
     * Endpoint para Verificação do Código (Ativação da Conta)
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody @Valid VerifyRequestDTO request) {
        authService.verify(request);
        return ResponseEntity.ok("Conta ativada com sucesso! Agora você já pode fazer login.");
    }
}
