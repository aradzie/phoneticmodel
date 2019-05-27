package com.keybr.phoneticmodel;

import java.util.Arrays;

final class Chain {

    final int order;

    final int size;

    final int spread;

    private final int[] pow;

    private final int[] chain;

    Chain(int order, int size) {
        if (order < 1 || size < 1) {
            throw new IllegalArgumentException();
        }
        this.order = order;
        this.size = size;
        this.spread = pow(size, order);
        this.pow = new int[order];
        for (int i = 0; i < order; i++) {
            this.pow[i] = pow(size, order - i - 1);
        }
        this.chain = new int[order];
    }

    void clear() {
        Arrays.fill(chain, 0);
    }

    boolean push(int value) {
        if (value < 0 || value >= size) {
            throw new IllegalArgumentException();
        }
        if (value > 0 || chain[order - 1] > 0) {
            System.arraycopy(chain, 1, chain, 0, chain.length - 1);
            chain[chain.length - 1] = value;
            return true;
        }
        else {
            return false;
        }
    }

    int toOffset() {
        int offset = 0;
        for (int i = 0; i < order; i++) {
            offset += chain[i] * pow[i];
        }
        return offset;
    }

    int[] fromOffset(int offset) {
        if (offset < 0 || offset >= spread) {
            throw new IllegalArgumentException();
        }
        int[] chars = new int[order];
        int value = offset;
        for (int i = 0; i < order; i++) {
            chars[i] = value / pow[i];
            value = value % pow[i];
        }
        return chars;
    }

    int[] toArray() {
        return Arrays.copyOf(chain, chain.length);
    }

    static int pow(int value, int pow) {
        if (pow < 0) {
            throw new IllegalArgumentException();
        }
        int r = 1;
        for (int i = 0; i < pow; i++) {
            r = Math.multiplyExact(r, value);
        }
        return r;
    }
}
