package com.keybr.phoneticmodel;

import java.text.Normalizer;
import java.util.Arrays;

enum Language {
    ENGLISH(" abcdefghijklmnopqrstuvwxyz"),
    FRENCH(" abcçdeéèfghijklmnopqrstuvwxyz"),
    GERMAN(" aäbcdefghijklmnoöpqrstuüvwxyzß"),
    ITALIAN(" abcdefghijklmnopqrstuvwxyz"),
    PORTUGUESE(" aáâãàbcçdeéêfghiíjklmnoóôõpqrstuúvwxyz"),
    RUSSIAN(" абвгдеёжзийклмнопрстуфхцчшщъыьэюя"),
    SPANISH(" abcdefghijklmnñopqrstuvwxyz");

    private final String alphabet;

    Language(String alphabet) {
        alphabet = Normalizer.normalize(alphabet, Normalizer.Form.NFC);
        for (int i = 0; i < alphabet.length(); i++) {
            if (i == 0) {
                if (alphabet.charAt(i) != ' ') {
                    throw new IllegalArgumentException();
                }
            }
            else {
                if (!Character.isLetter(alphabet.charAt(i))) {
                    throw new IllegalArgumentException();
                }
                if (!Character.isLowerCase(alphabet.charAt(i))) {
                    throw new IllegalArgumentException();
                }
            }
        }
        this.alphabet = alphabet;
    }

    public String alphabet() {
        return alphabet;
    }

    public static Language from(String name) {
        try {
            return Language.valueOf(name.toUpperCase());
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    String.format("Unknown language name %s, must be one of %s",
                            name, Arrays.asList(Language.values())));
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
