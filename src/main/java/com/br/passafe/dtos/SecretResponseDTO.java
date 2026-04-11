package com.br.passafe.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SecretResponseDTO {
    private Long id;
    private String title;
    private String siteUrl;
    private String usernameInSite;
    private String encryptedPassword;
    private String encryptedTotpKey;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastAutoFillAt;
}
