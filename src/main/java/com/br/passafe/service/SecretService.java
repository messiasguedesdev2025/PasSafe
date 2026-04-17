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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
        secret.setTitle(request.title());
        secret.setSiteUrl(request.siteUrl());
        secret.setUsernameInSite(request.usernameInSite());
        secret.setOwner(owner);
        secret.setEncryptedTotpKey(encryptionService.encrypt(request.password()));

        Secret saved = secretRepository.save(secret);
        log.info("Nova credencial salva para o usuário {}: {}", email, request.title());
        return convertToResponse(saved);
    }

    @Transactional
    public SecretResponseDTO update(Long id, SecretRequestDTO request, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Senha não encontrada!"));

        if (!secret.getOwner().getEmail().equals(email)) {
            log.error("Tentativa de acesso não autorizado de {} ao segredo ID {}", email, id);
            throw new RuntimeException("Você não tem permissão para editar esta senha!");
        }

        PasswordHistory history = new PasswordHistory();
        history.setEncryptedPassword(secret.getEncryptedTotpKey());
        history.setSecret(secret);
        passwordHistoryRepository.save(history);

        List<PasswordHistory> histories = passwordHistoryRepository.findBySecretOrderByCreatedAtDesc(secret);
        if (histories.size() > 5) {
            passwordHistoryRepository.deleteAll(histories.subList(5, histories.size()));
        }

        secret.setTitle(request.title());
        secret.setSiteUrl(request.siteUrl());
        secret.setUsernameInSite(request.usernameInSite());
        secret.setEncryptedTotpKey(encryptionService.encrypt(request.password()));

        Secret updated = secretRepository.save(secret);
        log.info("Credencial atualizada para {}: {}", email, request.title());
        return convertToResponse(updated);
    }

    // MÉTODO QUE ESTAVA FALTANDO
    @Transactional(readOnly = true)
    public SecretResponseDTO findById(Long id, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segredo não encontrado!"));

        if (!secret.getOwner().getEmail().equals(email)) {
            log.error("Acesso negado de {} ao segredo ID {}", email, id);
            throw new RuntimeException("Você não tem permissão para ver esta senha!");
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
        log.warn("Credencial ID {} deletada pelo usuário {}", id, email);
    }

    public List<SecretResponseDTO> findAllByUser(String email) {
        Usuario owner = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        return secretRepository.findByOwner(owner).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private SecretResponseDTO convertToResponse(Secret secret) {
        String decryptedPassword = null;
        if (secret.getEncryptedTotpKey() != null) {
            decryptedPassword = encryptionService.decrypt(secret.getEncryptedTotpKey());
        }

        return new SecretResponseDTO(
            secret.getId(),
            secret.getTitle(),
            secret.getSiteUrl(),
            secret.getUsernameInSite(),
            decryptedPassword,
            secret.getEncryptedTotpKey(),
            secret.getCreatedAt(),
            secret.getUpdatedAt(),
            secret.getLastAutoFillAt()
        );
    }
}
