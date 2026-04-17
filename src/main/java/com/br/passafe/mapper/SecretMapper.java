package com.br.passafe.mapper;

import com.br.passafe.dtos.SecretResponseDTO;
import com.br.passafe.entities.Secret;
import com.br.passafe.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecretMapper {

    private final EncryptionService encryptionService;

    public SecretResponseDTO toResponse(Secret secret) {
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
