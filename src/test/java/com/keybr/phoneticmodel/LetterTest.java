package com.keybr.phoneticmodel;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class LetterTest {

    @Test
    public void testScale_0() {
        var a = new Letter(0, 'a', 1);
        Letter[] letters = {a};

        assertThat(Letter.sumFrequencies(letters)).isNotEqualTo(255);

        Letter.scaleFrequencies(letters);

        assertThat(Letter.sumFrequencies(letters)).isEqualTo(255);
        assertThat(a.frequency).isEqualTo(255);
    }

    @Test
    public void testScale_1() {
        var a = new Letter(0, 'a', 1);
        var b = new Letter(1, 'b', 1);
        Letter[] letters = {a, b};

        assertThat(Letter.sumFrequencies(letters)).isNotEqualTo(255);

        Letter.scaleFrequencies(letters);

        assertThat(Letter.sumFrequencies(letters)).isEqualTo(255);
        assertThat(a.frequency).isEqualTo(127);
        assertThat(b.frequency).isEqualTo(128);
    }

    @Test
    public void testScale_2() {
        var a = new Letter(0, 'a', 1000);
        var b = new Letter(1, 'b', 1000);
        Letter[] letters = {a, b};

        assertThat(Letter.sumFrequencies(letters)).isNotEqualTo(255);

        Letter.scaleFrequencies(letters);

        assertThat(Letter.sumFrequencies(letters)).isEqualTo(255);
        assertThat(a.frequency).isEqualTo(127);
        assertThat(b.frequency).isEqualTo(128);
    }
}
