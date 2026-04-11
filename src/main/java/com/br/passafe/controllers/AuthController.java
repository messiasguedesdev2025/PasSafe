package com.br.passafe.controllers;

import com.br.passafe.dtos.LoginRequestDTO;
import com.br.passafe.dtos.UsuarioRequestDTO;
import com.br.passafe.dtos.VerifyRequestDTO;
import com.br.passafe.dtos.TokenResponseDTO;
import com.br.passafe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid UsuarioRequestDTO request) {
        authService.register(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@RequestBody @Valid VerifyRequestDTO request) {
        authService.verify(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}