package com.br.passafe.service;

import com.br.passafe.dtos.LoginRequestDTO;
import com.br.passafe.dtos.UsuarioRequestDTO;
import com.br.passafe.dtos.VerifyRequestDTO;
import com.br.passafe.entities.Usuario;
import com.br.passafe.exception.AuthException;
import com.br.passafe.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public void register(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new AuthException("Este e-mail já está em uso!");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username()); // Salva o nome de usuário
        usuario.setEmail(request.email());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setActivated(false);
        
        String code = String.format("%06d", new Random().nextInt(999999));
        usuario.setVerificationCode(code);
        usuario.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);

        log.info("REGISTRO: {} ({})", request.email(), request.username());
        log.warn("CÓDIGO DE VERIFICAÇÃO: {}", code);
    }

    public void verify(VerifyRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException("E-mail não encontrado!"));

        if (usuario.isActivated()) {
            throw new AuthException("Este usuário já está ativado!");
        }

        if (!usuario.getVerificationCode().equals(request.code())) {
            throw new AuthException("Código de verificação incorreto!");
        }

        if (usuario.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AuthException("O código expirou!");
        }

        usuario.setActivated(true);
        usuario.setVerificationCode(null);
        usuario.setVerificationCodeExpiresAt(null);
        usuarioRepository.save(usuario);
        
        log.info("Conta ativada com sucesso: {}", request.email());
    }

    public String login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException("E-mail ou senha incorretos!"));

        if (!usuario.isActivated()) {
            throw new AuthException("Por favor, ative sua conta primeiro!");
        }

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new AuthException("E-mail ou senha incorretos!");
        }

        return tokenService.generateToken(usuario.email());
    }
}
