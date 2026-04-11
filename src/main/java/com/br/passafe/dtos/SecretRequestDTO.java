package com.br.passafe.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SecretRequestDTO {
    @NotBlank(message = "O título é obrigatório")
    private String title;

    private String siteUrl;

    @NotBlank(message = "O usuário do site é obrigatório")
    private String usernameInSite;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    private String encryptedTotpKey; // Opcional (2FA)
}
