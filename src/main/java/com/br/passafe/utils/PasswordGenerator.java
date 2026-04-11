package com.br.passafe.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALL_CHARS = LOWER + UPPER + DIGITS + SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    /**
     * Gera uma senha forte aleatória.
     * @param length Tamanho da senha (mínimo 12 recomendado).
     * @return Uma senha contendo letras maiúsculas, minúsculas, números e símbolos.
     */
    public static String generateStrongPassword(int length) {
        if (length < 8) length = 8; // Garantir tamanho mínimo

        List<Character> passwordChars = new ArrayList<>();

        // Garantir pelo menos um de cada tipo para máxima segurança
        passwordChars.add(LOWER.charAt(random.nextInt(LOWER.length())));
        passwordChars.add(UPPER.charAt(random.nextInt(UPPER.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Preencher o restante com caracteres aleatórios de todos os tipos
        for (int i = 4; i < length; i++) {
            passwordChars.add(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Misturar a ordem dos caracteres para não serem previsíveis
        Collections.shuffle(passwordChars);

        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }

        return password.toString();
    }
}
