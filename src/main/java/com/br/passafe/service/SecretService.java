package com.br.passafe.service;

import com.br.passafe.dtos.SecretRequestDTO;
import com.br.passafe.dtos.SecretResponseDTO;
import com.br.passafe.entities.Secret;
import com.br.passafe.entities.Usuario;
import com.br.passafe.repositories.SecretRepository;
import com.br.passafe.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecretService {

    private final SecretRepository secretRepository;
    private final UsuarioRepository usuarioRepository;
    private final EncryptionService encryptionService;

    public SecretResponseDTO save(SecretRequestDTO request, String email) {
        Usuario owner = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Secret secret = new Secret();
        secret.setTitle(request.getTitle());
        secret.setSiteUrl(request.getSiteUrl());
        secret.setUsernameInSite(request.getUsernameInSite());
        secret.setOwner(owner);

        // ATENÇÃO: Verifique se no seu DTO o campo é 'password' ou 'encryptedPassword'
        String encrypted = encryptionService.encrypt(request.getPassword());
        secret.setEncryptedPassword(encrypted);

        Secret saved = secretRepository.save(secret);
        return convertToResponse(saved);
    }

    public List<SecretResponseDTO> findAllByUser(String email) {
        Usuario owner = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        return secretRepository.findByOwner(owner).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public SecretResponseDTO findById(Long id, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segredo não encontrado!"));

        if (!secret.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("Acesso negado!");
        }
        return convertToResponse(secret);
    }

    public void delete(Long id, String email) {
        Secret secret = secretRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segredo não encontrado!"));

        if (!secret.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("Acesso negado para exclusão!");
        }

        secretRepository.delete(secret);
    }

    private SecretResponseDTO convertToResponse(Secret secret) {
        SecretResponseDTO response = new SecretResponseDTO();
        response.setId(secret.getId());
        response.setTitle(secret.getTitle());
        response.setSiteUrl(secret.getSiteUrl());
        response.setUsernameInSite(secret.getUsernameInSite());

        // Descriptografa para enviar para a extensão
        String decrypted = encryptionService.decrypt(secret.getEncryptedPassword());

        // ATENÇÃO: Verifique se no seu ResponseDTO o método é setPassword ou setDecryptedPassword
        response.setPassword(decrypted);
        response.setCreatedAt(secret.getCreatedAt());
        response.setUpdatedAt(secret.getUpdatedAt());
        response.setEncryptedTotpKey(secret.getEncryptedTotpKey());

        return response;
    }
}