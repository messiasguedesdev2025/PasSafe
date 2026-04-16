package com.br.passafe.service;

import com.br.passafe.dtos.LoginRequestDTO;
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
    private final TokenService tokenService;

    public void register(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Este e-mail já está em uso!");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setActivated(false);
        
        String code = String.format("%06d", new Random().nextInt(999999));
        usuario.setVerificationCode(code);
        usuario.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);

        System.out.println("CÓDIGO DE VERIFICAÇÃO PARA " + request.email() + ": " + code);
    }

    public void verify(VerifyRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado!"));

        if (usuario.isActivated()) {
            throw new RuntimeException("Este usuário já está ativado!");
        }

        if (!usuario.getVerificationCode().equals(request.code())) {
            throw new RuntimeException("Código de verificação incorreto!");
        }

        if (usuario.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("O código expirou!");
        }

        usuario.setActivated(true);
        usuario.setVerificationCode(null);
        usuario.setVerificationCodeExpiresAt(null);
        usuarioRepository.save(usuario);
    }

    public String login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha incorretos!"));

        if (!usuario.isActivated()) {
            throw new RuntimeException("Por favor, ative sua conta primeiro!");
        }

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new RuntimeException("E-mail ou senha incorretos!");
        }

        // CORREÇÃO: Entidade usa getEmail(), Record usa email()
        return tokenService.generateToken(usuario.getEmail());
    }
}
