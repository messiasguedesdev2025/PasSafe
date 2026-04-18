package com.br.passafe.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 3, max = 20, message = "O nome de usuário deve ter entre 3 e 20 caracteres")
    String username,

    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 12, message = "A senha deve ter pelo menos 12 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=*!]).*$",
        message = "A senha deve conter maiúscula, minúscula, número e caractere especial"
    )
    String password
) {}
