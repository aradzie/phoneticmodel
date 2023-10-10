package com.keybr.phoneticmodel;

import java.text.Normalizer;

class Alphabet {
    final String alphabet;

    Alphabet(String alphabet) {
        alphabet = Normalizer.normalize(alphabet, Normalizer.Form.NFC);
        for (int i = 0; i < alphabet.length(); i++) {
            if (!(Character.isLetter(alphabet.charAt(i)) && Character.isLowerCase(alphabet.charAt(i)))) {
                throw new IllegalArgumentException("The alphabet string can only contain lowercase letters");
            }
        }
        this.alphabet = " " + alphabet;
    }

    public String alphabet() {
        return alphabet;
    }
}
