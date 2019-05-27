package com.keybr.phoneticmodel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

final class Compressor {

    static ByteBuffer compress(TransitionTable table) {
        var order = table.order;
        var alphabet = table.alphabet;
        var size = table.size;

        var buffer = ByteBuffer.allocate(256 * 1024).order(ByteOrder.BIG_ENDIAN);

        // File signature.
        buffer.put((byte) 'k');
        buffer.put((byte) 'e');
        buffer.put((byte) 'y');
        buffer.put((byte) 'b');
        buffer.put((byte) 'r');
        buffer.put((byte) '.');
        buffer.put((byte) 'c');
        buffer.put((byte) 'o');
        buffer.put((byte) 'm');

        // Markov chain order.
        buffer.put((byte) order);

        // Alphabet size, including space.
        buffer.put((byte) size);

        // Alphabet letters in UTF-16.
        for (int i = 0; i < size; i++) {
            buffer.putChar(alphabet.charAt(i));
        }

        int strides = Chain.pow(size, order - 1);
        for (int stride = 0; stride < strides; stride++) {
            var letters = new ArrayList<Letter>(size);
            for (int i = 0; i < size; i++) {
                int frequency = table.get(stride * size + i);
                if (frequency > 0) {
                    letters.add(new Letter(i, frequency));
                }
            }
            Letter.scaleFrequencies(letters);
            // Letter frequencies in this stride.
            buffer.put((byte) letters.size());
            for (Letter letter : letters) {
                buffer.put((byte) letter.index());
                buffer.put((byte) letter.frequency());
            }
        }

        return buffer;
    }
}
