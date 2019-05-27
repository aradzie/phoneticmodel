package com.keybr.phoneticmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

final class Letter {

    static final Comparator<Letter> SORT_BY_INDEX =
            Comparator.comparingInt((Letter letter) -> letter.index);

    static final Comparator<Letter> SORT_BY_FREQUENCY =
            Comparator.comparingInt((Letter letter) -> letter.frequency).reversed();

    private final int index;

    private int frequency;

    Letter(int index, int frequency) {
        this.index = index;
        this.frequency = frequency;
    }

    int index() {
        return index;
    }

    int frequency() {
        return frequency;
    }

    static int sumFrequencies(Collection<Letter> letters) {
        return letters.stream().mapToInt(letter -> letter.frequency).sum();
    }

    static void scaleFrequencies(Collection<Letter> letters) {
        if (!letters.isEmpty()) {
            var sorted = new ArrayList<>(letters);
            sorted.sort(SORT_BY_FREQUENCY);
            scaleRough(sorted);
            scaleFine(sorted);
        }
    }

    private static void scaleRough(ArrayList<Letter> letters) {
        int sum = sumFrequencies(letters);
        for (var letter : letters) {
            if (letter.frequency < 1) {
                throw new IllegalArgumentException();
            }
            letter.frequency = Math.max(1, Math.round(255f / sum * letter.frequency));
        }
    }

    private static void scaleFine(ArrayList<Letter> letters) {
        int sum = sumFrequencies(letters);
        while (sum > 255) {
            int i = 0;
            while (sum > 255 && i < letters.size()) {
                var letter = letters.get(i);
                if (letter.frequency > 1) {
                    letter.frequency--;
                    sum--;
                }
                i++;
            }
        }
        while (sum < 255) {
            int i = 0;
            while (sum < 255 && i < letters.size()) {
                var letter = letters.get(i);
                letter.frequency++;
                sum++;
                i++;
            }
        }
    }
}
