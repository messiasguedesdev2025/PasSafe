package com.br.passafe.dtos;

import java.time.LocalDateTime;

public record SecretResponseDTO(
    Long id,
    String title,
    String siteUrl,
    String usernameInSite,
    String password, // Senha descriptografada para o cliente
    String encryptedTotpKey,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime lastAutoFillAt
) {}
