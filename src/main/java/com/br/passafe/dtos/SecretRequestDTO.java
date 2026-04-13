package com.br.passafe.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SecretRequestDTO {

    @NotBlank(message = "O título é obrigatório (ex: GitHub, Google)")
    @Size(min = 2, max = 50, message = "O título deve ter entre 2 e 50 caracteres")
    private String title;

    @NotBlank(message = "O site ou URL é obrigatório")
    private String siteUrl;

    @NotBlank(message = "O nome de usuário ou e-mail do site é obrigatório")
    private String usernameInSite;

    @NotBlank(message = "A senha é obrigatória")
    private String password;

    private String encryptedTotpKey; // Opcional (2FA)
}
