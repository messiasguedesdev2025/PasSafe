package com.br.passafe.service;

import com.br.passafe.dtos.UsuarioRequestDTO;
import com.br.passafe.dtos.VerifyRequestDTO;
import com.br.passafe.entities.Usuario;
import com.br.passafe.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Este e-mail já está em uso!");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setActivated(false);
        
        // Gera o código de 6 dígitos
        String code = String.format("%06d", new Random().nextInt(999999));
        usuario.setVerificationCode(code);
        usuario.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15)); // Expira em 15 min

        usuarioRepository.save(usuario);

        // TODO: Enviar o e-mail com o código (usar JavaMailSender no futuro)
        System.out.println("CÓDIGO DE VERIFICAÇÃO PARA " + request.getEmail() + ": " + code);
    }

    public void verify(VerifyRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado!"));

        if (usuario.isActivated()) {
            throw new RuntimeException("Este usuário já está ativado!");
        }

        if (!usuario.getVerificationCode().equals(request.getCode())) {
            throw new RuntimeException("Código de verificação incorreto!");
        }

        if (usuario.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("O código expirou! Gere um novo.");
        }

        usuario.setActivated(true);
        usuario.setVerificationCode(null);
        usuario.setVerificationCodeExpiresAt(null);
        usuarioRepository.save(usuario);
    }
}
