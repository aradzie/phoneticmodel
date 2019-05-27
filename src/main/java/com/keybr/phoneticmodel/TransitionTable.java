package com.keybr.phoneticmodel;

import javax.json.Json;
import javax.json.JsonValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;

final class TransitionTable
        implements Iterable<TransitionTable.Cursor>, Jsonable {

    static final class Cursor {

        final int[] chars;

        final int offset;

        Cursor(int[] chars, int offset) {
            this.chars = chars;
            this.offset = offset;
        }
    }

    /** Markov chain order. */
    final int order;

    /** The set of allowed characters, including space. */
    final String alphabet;

    /** Alphabet size, including space. */
    final int size;

    /** Fixed size chain of character indexes. */
    private final Chain chain;

    /** Number of occurrences of character sequences. */
    private final int[] frequencies;

    TransitionTable(int order, String alphabet) {
        if (order < 1) {
            throw new IllegalArgumentException("Order cannot be less than 1");
        }
        if (alphabet.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (alphabet.charAt(0) != ' ') {
            throw new IllegalArgumentException();
        }
        this.order = order;
        this.alphabet = alphabet;
        this.size = alphabet.length();
        this.chain = new Chain(order, size);
        this.frequencies = new int[chain.spread];
    }

    void scan(CharSequence text) {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = Character.toLowerCase(text.charAt(i));
            if (alphabet.indexOf(ch) < 0) {
                ch = ' ';
            }
            if (ch == ' ') {
                push(word);
                word.setLength(0);
            }
            else {
                word.append(ch);
            }
        }
        push(word);
    }

    void push(CharSequence word) {
        if (word.length() >= 3) {
            for (int i = 0; i < word.length(); i++) {
                push(word.charAt(i));
            }
            push(' ');
        }
    }

    void push(char ch) {
        int index = alphabet.indexOf(ch);
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        if (chain.push(index)) {
            frequencies[chain.toOffset()] += 1;
        }
        if (ch == ' ') {
            chain.clear();
        }
    }

    int length() {
        return frequencies.length;
    }

    int get(int index) {
        return frequencies[index];
    }

    void set(int index, int value) {
        frequencies[index] = value;
    }

    @Override
    public Iterator<Cursor> iterator() {
        return new CursorIterator();
    }

    @Override
    public JsonValue toJson() {
        int sum = IntStream.of(frequencies).sum();
        var json = Json.createArrayBuilder();
        for (var cursor : this) {
            int frequency = frequencies[cursor.offset];
            if (frequency > 0) {
                var entry = Json.createArrayBuilder();
                for (int i = 0; i < order; i++) {
                    entry.add(alphabet.charAt(cursor.chars[i]));
                }
                entry.add((double) frequency / (double) sum);
                json.add(entry.build());
            }
        }
        return json.build();
    }

    void writeBinary(Path path)
            throws IOException {
        var buffer = Compressor.compress(this);
        try (var out = Files.newOutputStream(path)) {
            out.write(buffer.array(), 0, buffer.position());
        }
    }

    void writeJson(Path path)
            throws IOException {
        var json = toJson();
        try (var writer = Files.newBufferedWriter(path, UTF_8)) {
            Jsonable.write(writer, json);
        }
    }

    private class CursorIterator
            implements Iterator<Cursor> {

        int offset = 0;

        @Override
        public boolean hasNext() {
            return offset < frequencies.length;
        }

        @Override
        public Cursor next() {
            if (hasNext()) {
                var cursor = new Cursor(chain.fromOffset(offset), offset);
                offset += 1;
                return cursor;
            }
            else {
                throw new NoSuchElementException();
            }
        }
    }
}
