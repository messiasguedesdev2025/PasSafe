package com.br.passafe.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SecretRequestDTO(
    @NotBlank(message = "O título é obrigatório (ex: GitHub, Google)")
    @Size(min = 2, max = 50, message = "O título deve ter entre 2 e 50 caracteres")
    String title,

    @NotBlank(message = "O site ou URL é obrigatório")
    String siteUrl,

    @NotBlank(message = "O nome de usuário ou e-mail do site é obrigatório")
    String usernameInSite,

    @NotBlank(message = "A senha é obrigatória")
    String password,

    String encryptedTotpKey // Opcional (2FA)
) {}
