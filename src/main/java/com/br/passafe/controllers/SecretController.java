package com.br.passafe.controllers;

import com.br.passafe.dtos.SecretRequestDTO;
import com.br.passafe.dtos.SecretResponseDTO;
import com.br.passafe.service.SecretService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secrets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Libera acesso de qualquer app/extensão
public class SecretController {

    private final SecretService secretService;

    /**
     * Lista todas as senhas do usuário logado.
     */
    @GetMapping
    public ResponseEntity<List<SecretResponseDTO>> getAllSecrets() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(secretService.findAllByUser(email));
    }

    /**
     * Busca uma senha específica pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SecretResponseDTO> getSecretById(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(secretService.findById(id, email));
    }

    /**
     * Salva uma nova senha no cofre.
     */
    @PostMapping
    public ResponseEntity<SecretResponseDTO> createSecret(@RequestBody @Valid SecretRequestDTO request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(201).body(secretService.save(request, email));
    }

    /**
     * Atualiza uma senha e guarda a antiga no histórico (máximo 5).
     */
    @PutMapping("/{id}")
    public ResponseEntity<SecretResponseDTO> updateSecret(@PathVariable Long id, @RequestBody @Valid SecretRequestDTO request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(secretService.update(id, request, email));
    }

    /**
     * Deleta uma senha do cofre.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSecret(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        secretService.delete(id, email);
        return ResponseEntity.noContent().build();
    }
}
