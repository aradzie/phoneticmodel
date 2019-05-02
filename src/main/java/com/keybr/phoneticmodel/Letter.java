package com.keybr.phoneticmodel;

import java.util.Arrays;
import java.util.Comparator;

final class Letter {

    static final Comparator<Letter> CMP_BY_FREQUENCY_DESC = (o1, o2) -> {
        int i = o2.frequency - o1.frequency;
        if (i == 0) {
            return o1.index - o2.index;
        }
        return i;
    };

    final int index;

    final char character;

    int frequency;

    Letter(int index, char character, int frequency) {
        this.index = index;
        this.character = character;
        this.frequency = frequency;
    }

    static void scaleFrequencies(Letter[] letters) {
        int sum;

        // TODO Ensure that non-zero frequencies do not get scaled to zero.

        Arrays.sort(letters, Letter.CMP_BY_FREQUENCY_DESC);

        sum = sumFrequencies(letters);
        // Scale frequencies so that their sum is 255.
        for (var letter : letters) {
            if (letter.frequency > 0) {
                letter.frequency = Math.max(1, Math.round(255f / sum * letter.frequency));
            }
        }

        sum = sumFrequencies(letters);
        if (sum > 255) {
            int i = 0;
            while (sum > 255) {
                letters[i].frequency--;
                sum--;
                i++;
            }
        }
        else if (sum < 255) {
            int i = 0;
            while (sum < 255) {
                letters[i].frequency++;
                sum++;
                i++;
            }
        }
    }

    static int sumFrequencies(Letter[] letters) {
        int sum = 0;
        for (var letter : letters) {
            sum += letter.frequency;
        }
        return sum;
    }
}
