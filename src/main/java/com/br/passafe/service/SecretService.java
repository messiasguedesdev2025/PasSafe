package com.br.passafe.service;

import com.br.passafe.dtos.SecretRequestDTO;
import com.br.passafe.dtos.SecretResponseDTO;
import com.br.passafe.entities.PasswordHistory;
import com.br.passafe.entities.Secret;
import com.br.passafe.entities.Usuario;
import com.br.passafe.exception.ResourceNotFoundException;
import com.br.passafe.mapper.SecretMapper;
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
    private final SecretMapper secretMapper;

    @Transactional
    public SecretResponseDTO save(SecretRequestDTO request, String email) {
        Usuario owner = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        Secret secret = new Secret();
        secret.setTitle(request.title());
        secret.setSiteUrl(request.siteUrl());
        secret.setUsernameInSite(request.usernameInSite());
        secret.setOwner(owner);
        secret.setEncryptedTotpKey(encryptionService.encrypt(request.password()));

        Secret saved = secretRepository.save(secret);
        log.info("Credencial '{}' salva com sucesso para o usuário {}", saved.getTitle(), email);
        return secretMapper.toResponse(saved);
    }

    @Transactional
    public SecretResponseDTO update(Long id, SecretRequestDTO request, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Senha não encontrada!"));

        validateOwnership(secret, email);

        // Histórico
        PasswordHistory history = new PasswordHistory();
        history.setEncryptedPassword(secret.getEncryptedTotpKey());
        history.setSecret(secret);
        passwordHistoryRepository.save(history);

        // Limpeza de histórico
        List<PasswordHistory> histories = passwordHistoryRepository.findBySecretOrderByCreatedAtDesc(secret);
        if (histories.size() > 5) {
            passwordHistoryRepository.deleteAll(histories.subList(5, histories.size()));
        }

        secret.setTitle(request.title());
        secret.setSiteUrl(request.siteUrl());
        secret.setUsernameInSite(request.usernameInSite());
        secret.setEncryptedTotpKey(encryptionService.encrypt(request.password()));

        Secret updated = secretRepository.save(secret);
        log.info("Credencial ID {} atualizada pelo usuário {}", id, email);
        return secretMapper.toResponse(updated);
    }

    @Transactional(readOnly = true)
    public SecretResponseDTO findById(Long id, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Segredo não encontrado!"));

        validateOwnership(secret, email);
        return secretMapper.toResponse(secret);
    }

    @Transactional
    public void delete(Long id, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Segredo não encontrado!"));

        validateOwnership(secret, email);
        secretRepository.delete(secret);
        log.warn("Credencial ID {} removida definitivamente por {}", id, email);
    }

    @Transactional(readOnly = true)
    public List<SecretResponseDTO> findAllByUser(String email) {
        Usuario owner = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        return secretRepository.findByOwner(owner).stream()
                .map(secretMapper::toResponse)
                .collect(Collectors.toList());
    }

    private void validateOwnership(Secret secret, String email) {
        if (!secret.getOwner().getEmail().equals(email)) {
            log.error("ACESSO NEGADO: Usuário {} tentou acessar recurso do ID {}", email, secret.getId());
            throw new RuntimeException("Acesso negado a este recurso!");
        }
    }
}
