package com.br.passafe.service;

import com.br.passafe.dtos.SecretRequestDTO;
import com.br.passafe.dtos.SecretResponseDTO;
import com.br.passafe.entities.PasswordHistory;
import com.br.passafe.entities.Secret;
import com.br.passafe.entities.Usuario;
import com.br.passafe.repositories.PasswordHistoryRepository;
import com.br.passafe.repositories.SecretRepository;
import com.br.passafe.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecretService {

    private final SecretRepository secretRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final EncryptionService encryptionService;

    @Transactional
    public SecretResponseDTO save(SecretRequestDTO request, String email) {
        Usuario owner = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Secret secret = new Secret();
        secret.setTitle(request.getTitle());
        secret.setSiteUrl(request.getSiteUrl());
        secret.setUsernameInSite(request.getUsernameInSite());
        secret.setOwner(owner);
        secret.setEncryptedTotpKey(encryptionService.encrypt(request.getPassword()));

        Secret saved = secretRepository.save(secret);
        return convertToResponse(saved);
    }

    @Transactional
    public SecretResponseDTO update(Long id, SecretRequestDTO request, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Senha não encontrada!"));

        if (!secret.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("Você não tem permissão para editar esta senha!");
        }

        // --- Lógica de Histórico de 5 Senhas ---
        PasswordHistory history = new PasswordHistory();
        history.setEncryptedPassword(secret.getEncryptedTotpKey());
        history.setSecret(secret);
        passwordHistoryRepository.save(history);

        // Busca o histórico e mantém apenas as 5 mais recentes
        List<PasswordHistory> histories = passwordHistoryRepository.findBySecretOrderByCreatedAtDesc(secret);
        if (histories.size() > 5) {
            passwordHistoryRepository.deleteAll(histories.subList(5, histories.size()));
        }
        // --------------------------------------

        secret.setTitle(request.getTitle());
        secret.setSiteUrl(request.getSiteUrl());
        secret.setUsernameInSite(request.getUsernameInSite());
        secret.setEncryptedTotpKey(encryptionService.encrypt(request.getPassword()));

        Secret updated = secretRepository.save(secret);
        return convertToResponse(updated);
    }

    @Transactional
    public SecretResponseDTO findById(Long id, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Senha não encontrada!"));

        if (!secret.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("Acesso negado!");
        }
        return convertToResponse(secret);
    }

    @Transactional
    public void delete(Long id, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Senha não encontrada!"));

        if (!secret.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("Você não tem permissão para deletar esta senha!");
        }

        secretRepository.delete(secret);
    }

    public List<SecretResponseDTO> findAllByUser(String email) {
        Usuario owner = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        return secretRepository.findByOwner(owner).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private SecretResponseDTO convertToResponse(Secret secret) {
        SecretResponseDTO response = new SecretResponseDTO();
        response.setId(secret.getId());
        response.setTitle(secret.getTitle());
        response.setSiteUrl(secret.getSiteUrl());
        response.setUsernameInSite(secret.getUsernameInSite());
        
        if (secret.getEncryptedTotpKey() != null) {
            response.setPassword(encryptionService.decrypt(secret.getEncryptedTotpKey()));
        }
        
        response.setCreatedAt(secret.getCreatedAt());
        response.setUpdatedAt(secret.getUpdatedAt());
        return response;
    }
}
