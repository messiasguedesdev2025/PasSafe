package com.br.passafe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;

    @Value("${passafe.encryption.key}")
    private String secretKey; // Chave mestra definida no application.properties (32 caracteres para AES-256)

    /**
     * Criptografa um texto usando AES-256-GCM.
     */
    public String encrypt(String plainText) {
        try {
            byte[] iv = new byte[IV_LENGTH_BYTE];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec);
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Concatena o IV (vetor de inicialização) com o texto cifrado
            byte[] cipherTextWithIv = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, cipherTextWithIv, 0, iv.length);
            System.arraycopy(cipherText, 0, cipherTextWithIv, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(cipherTextWithIv);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar dados: " + e.getMessage());
        }
    }

    /**
     * Descriptografa um texto cifrado em Base64.
     */
    public String decrypt(String encryptedText) {
        try {
            byte[] decode = Base64.getDecoder().decode(encryptedText);

            byte[] iv = new byte[IV_LENGTH_BYTE];
            System.arraycopy(decode, 0, iv, 0, iv.length);

            byte[] cipherText = new byte[decode.length - IV_LENGTH_BYTE];
            System.arraycopy(decode, iv.length, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            cipher.init(Cipher.DECRYPT_MODE, keySpec, parameterSpec);
            byte[] decryptedText = cipher.doFinal(cipherText);

            return new String(decryptedText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar dados: " + e.getMessage());
        }
    }
}
