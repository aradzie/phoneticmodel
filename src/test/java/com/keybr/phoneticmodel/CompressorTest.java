package com.keybr.phoneticmodel;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class CompressorTest {

    @Test
    public void testGenerate() {
        var table = new TransitionTable(3, " abcdefghijklmnopqrstuvwxyz");
        table.scan("hello world");

        var buffer = Compressor.compress(table);

        assertThat(buffer.position()).isEqualTo(1561);
    }
}
