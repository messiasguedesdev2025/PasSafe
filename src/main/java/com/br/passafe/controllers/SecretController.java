package com.br.passafe.controllers;

import com.br.passafe.dtos.SecretRequestDTO;
import com.br.passafe.dtos.SecretResponseDTO;
import com.br.passafe.service.SecretService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/secrets")
@RequiredArgsConstructor
public class SecretController {

    private final SecretService secretService;

    @GetMapping
    public ResponseEntity<List<SecretResponseDTO>> getAllSecrets() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(secretService.findAllByUser(email));
    }

    @PostMapping
    public ResponseEntity<SecretResponseDTO> createSecret(@RequestBody SecretRequestDTO request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(201).body(secretService.save(request, email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecretResponseDTO> getSecretById(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(secretService.findById(id, email));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSecret(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        secretService.delete(id, email);
        return ResponseEntity.noContent().build();
    }
}