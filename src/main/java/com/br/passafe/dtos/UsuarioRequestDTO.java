package com.br.passafe.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 12, message = "A senha deve ter pelo menos 12 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=*!]).*$",
        message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial (@#$%^&+=*!)"
    )
    private String password;
}
