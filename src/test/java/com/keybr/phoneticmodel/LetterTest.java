package com.keybr.phoneticmodel;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public final class LetterTest {

    @Test
    public void scale1() {
        var a = new Letter(0, 1);
        List<Letter> letters = List.of(a);

        assertThat(Letter.sumFrequencies(letters)).isNotEqualTo(255);

        Letter.scaleFrequencies(letters);

        assertThat(Letter.sumFrequencies(letters)).isEqualTo(255);
        assertThat(a.frequency()).isEqualTo(255);
    }

    @Test
    public void scale2() {
        var a = new Letter(0, 1);
        var b = new Letter(1, 1);
        List<Letter> letters = List.of(a, b);

        assertThat(Letter.sumFrequencies(letters)).isNotEqualTo(255);

        Letter.scaleFrequencies(letters);

        assertThat(Letter.sumFrequencies(letters)).isEqualTo(255);
        assertThat(a.frequency()).isEqualTo(127);
        assertThat(b.frequency()).isEqualTo(128);
    }

    @Test
    public void scale3() {
        var a = new Letter(0, 1000);
        var b = new Letter(1, 1000);
        List<Letter> letters = List.of(a, b);

        assertThat(Letter.sumFrequencies(letters)).isNotEqualTo(255);

        Letter.scaleFrequencies(letters);

        assertThat(Letter.sumFrequencies(letters)).isEqualTo(255);
        assertThat(a.frequency()).isEqualTo(127);
        assertThat(b.frequency()).isEqualTo(128);
    }

    @Test
    public void scale4() {
        var a = new Letter(0, 1);
        var b = new Letter(1, 1000);
        List<Letter> letters = List.of(a, b);

        assertThat(Letter.sumFrequencies(letters)).isNotEqualTo(255);

        Letter.scaleFrequencies(letters);

        assertThat(Letter.sumFrequencies(letters)).isEqualTo(255);
        assertThat(a.frequency()).isEqualTo(1);
        assertThat(b.frequency()).isEqualTo(254);
    }
}
