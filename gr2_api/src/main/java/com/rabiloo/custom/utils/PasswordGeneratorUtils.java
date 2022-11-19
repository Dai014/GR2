package com.rabiloo.custom.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGeneratorUtils {

    private static final int DEFAULT_PASSWORD_LENGTH = 10;

    private static final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_LOWERCASE_FOR_USER = "abcdefghijkmnopqrstuvwxyz";
    private static final String CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase();
    private static final String CHAR_UPPERCASE_FOR_USER = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String DIGIT = "0123456789";
    private static final String DIGIT_FOR_USER = "123456789";
    private static final String OTHER_PUNCTUATION = "!@#&()–[{}]:;',?/*";
    private static final String OTHER_SYMBOL = "~$^+=<>";
    private static final String OTHER_SPECIAL = OTHER_PUNCTUATION + OTHER_SYMBOL;
    private static final int PASSWORD_LENGTH = 8;

    private static final String PASSWORD_ALLOW_FOR_USER =
            CHAR_LOWERCASE_FOR_USER + CHAR_UPPERCASE_FOR_USER + DIGIT_FOR_USER + OTHER_SPECIAL;

    private static SecureRandom random = new SecureRandom();

    public static String generateDefaultPasswordForUser() {
        String password = generateRandomString(PASSWORD_ALLOW_FOR_USER, PASSWORD_LENGTH);
        shuffleString(password);

        return password;
    }

    private static String generateRandomString(String input, int size) {

        if (input == null || input.length() <= 0)
            throw new IllegalArgumentException("Invalid input.");
        if (size < 1) throw new IllegalArgumentException("Invalid size.");

        StringBuilder result = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(input.length());
            result.append(input.charAt(index));
        }
        return result.toString();
    }

    public static String shuffleString(String input) {
        List<String> result = Arrays.asList(input.split(""));
        Collections.shuffle(result);
        return result.stream().collect(Collectors.joining());
    }

    public static String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return RandomStringUtils.random(DEFAULT_PASSWORD_LENGTH, characters);
    }

}
