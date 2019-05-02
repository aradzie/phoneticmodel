package com.keybr.phoneticmodel;

import java.nio.ByteBuffer;

/**
 * File format:
 * <pre>
 * POS      SIZE    Description
 * -----------------------------------------------
 * 0-8      9       Literal string "keybr.com"
 * 9-9      1       Alphabet length N
 * 10-X     2*N     Char codes of alphabet letters in UTF-16
 *          1       Markov chain order
 *          X       Index data
 *          Y       Frequency data
 * </pre>
 */
final class Compressor {

    static ByteBuffer compress(TransitionTable table) {
        var order = table.order;
        var alphabet = table.alphabet;
        var size = table.size;

        var buffer = ByteBuffer.allocate(256 * 1024);

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

        // Alphabet size, including space.
        buffer.put((byte) size);

        // Alphabet letters in UTF-16.
        for (int i = 0; i < size; i++) {
            buffer.putChar(alphabet.charAt(i));
        }

        // Markov chain order.
        buffer.put((byte) order);

        // Allocate space for index and data.
        int indexOffset = buffer.position();
        int indexSize = Chain.pow(size, order - 1) * 2;
        int dataOffset = indexOffset + indexSize;

        buffer.position(dataOffset);

        int strides = Chain.pow(size, order - 1);
        for (int stride = 0; stride < strides; stride++) {
            int offset = stride * size;

            // Fill in letter frequencies for this stride.
            var letters = new Letter[size];
            for (int i = 0; i < size; i++) {
                letters[i] = new Letter(
                        i,
                        alphabet.charAt(i),
                        table.valueAt(offset + i));
            }

            int indexEntry = indexOffset + stride * 2;

            if (Letter.sumFrequencies(letters) > 0) {
                // Scale frequencies.
                Letter.scaleFrequencies(letters);

                // Write offset to the compressed frequencies array in index.
                buffer.putShort(indexEntry, (short) (buffer.position() - dataOffset));

                // Compress frequencies and write to the data file.
                compress(letters, size, buffer);
            }
            else {
                // Frequencies array is empty.
                buffer.putShort(indexEntry, (short) -1);
            }
        }

        int dataSize = buffer.position() - dataOffset;
        if (dataSize > 0xFFFF) {
            throw new IndexOutOfBoundsException("Data segment is too large");
        }

        return buffer;
    }

    private static void compress(Letter[] letters, int size, ByteBuffer buffer) {
        // Array of frequencies.
        int[] d = new int[size];
        for (int i = 0; i < size; i++) {
            d[letters[i].index] = letters[i].frequency;
        }

        // Compress frequencies array using run-length encoding.
        int p = 0;
        while (p < size) {
            int x = 0, y = 0;
            while (p + x < size) {
                if (d[p + x] == 0) {
                    break;
                }
                if (x == 15) {
                    break;
                }
                x++;
            }
            while (p + x + y < size) {
                if (d[p + x + y] != 0) {
                    break;
                }
                if (y == 15) {
                    break;
                }
                y++;
            }
            buffer.put((byte) (x << 4 | y));
            for (int i = 0; i < x; i++) {
                buffer.put((byte) d[p + i]);
            }
            p = p + x + y;
        }
    }
}
