package com.keybr.phoneticmodel;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public final class CompressorTest {

    @Test
    public void compress() {
        var table = new TransitionTable(2, " ab");
        table.set(1, 1);
        table.set(2, 1);
        table.set(8, 1);

        var buffer = Compressor.compress(table);

        assertThat(Arrays.copyOfRange(buffer.array(), 0, buffer.position())).isEqualTo(new byte[] {
                // signature
                0x6B, 0x65, 0x79, 0x62, 0x72, 0x2E, 0x63, 0x6F, 0x6D,
                // order
                0x02,
                // alphabet size
                0x03,
                // alphabet
                0x00, 0x20, 0x00, 0x61, 0x00, 0x62,
                // frequencies, stride 0
                0x02, 0x01, 0x7F, 0x02, (byte) 0x80,
                // frequencies, stride 1
                0x00,
                // frequencies, stride 2
                0x01, 0x02, (byte) 0xFF,
        });
    }
}
