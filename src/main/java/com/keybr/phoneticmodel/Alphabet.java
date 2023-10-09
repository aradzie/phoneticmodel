package com.keybr.phoneticmodel;

import java.text.Normalizer;

// ENGLISH(" abcdefghijklmnopqrstuvwxyz")
// FRENCH(" abcçdeéèfghijklmnopqrstuvwxyz")
// GERMAN(" aäbcdefghijklmnoöpqrstuüvwxyzß")
// ITALIAN(" abcdefghijklmnopqrstuvwxyz")
// PORTUGUESE(" aáâãàbcçdeéêfghiíjklmnoóôõpqrstuúvwxyz")
// RUSSIAN(" абвгдеёжзийклмнопрстуфхцчшщъыьэюя")
// SPANISH(" abcdefghijklmnñopqrstuvwxyz")

class Alphabet {
    final String alphabet;

    Alphabet(String alphabet) {
        alphabet = Normalizer.normalize(alphabet, Normalizer.Form.NFC);
        for (int i = 0; i < alphabet.length(); i++) {
            if (i == 0) {
                if (alphabet.charAt(i) != ' ') {
                    throw new IllegalArgumentException("Alphabet must begin with the space character");
                }
            }
            else {
                if (!Character.isLetter(alphabet.charAt(i))) {
                    throw new IllegalArgumentException("Alphabet contains a non-letter character");
                }
                if (!Character.isLowerCase(alphabet.charAt(i))) {
                    throw new IllegalArgumentException("Alphabet contains a non-lowercase letter character");
                }
            }
        }
        this.alphabet = alphabet;
    }

    public String alphabet() {
        return alphabet;
    }
}
