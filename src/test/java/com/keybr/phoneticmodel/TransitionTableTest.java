package com.keybr.phoneticmodel;

import com.keybr.phoneticmodel.TransitionTable.Cursor;
import org.junit.Test;

import javax.json.Json;

import static org.assertj.core.api.Assertions.assertThat;

public final class TransitionTableTest {

    @Test
    public void testIterate() {
        var table = new TransitionTable(2, " ab");

        Cursor cursor;
        var iterator = table.iterator();

        cursor = iterator.next();
        assertThat(cursor.offset).isEqualTo(0);
        assertThat(cursor.chars).isEqualTo(new int[] {0, 0});

        cursor = iterator.next();
        assertThat(cursor.offset).isEqualTo(1);
        assertThat(cursor.chars).isEqualTo(new int[] {0, 1});

        cursor = iterator.next();
        assertThat(cursor.offset).isEqualTo(2);
        assertThat(cursor.chars).isEqualTo(new int[] {0, 2});

        cursor = iterator.next();
        assertThat(cursor.offset).isEqualTo(3);
        assertThat(cursor.chars).isEqualTo(new int[] {1, 0});

        cursor = iterator.next();
        assertThat(cursor.offset).isEqualTo(4);
        assertThat(cursor.chars).isEqualTo(new int[] {1, 1});

        cursor = iterator.next();
        assertThat(cursor.offset).isEqualTo(5);
        assertThat(cursor.chars).isEqualTo(new int[] {1, 2});

        cursor = iterator.next();
        assertThat(cursor.offset).isEqualTo(6);
        assertThat(cursor.chars).isEqualTo(new int[] {2, 0});

        cursor = iterator.next();
        assertThat(cursor.offset).isEqualTo(7);
        assertThat(cursor.chars).isEqualTo(new int[] {2, 1});

        cursor = iterator.next();
        assertThat(cursor.offset).isEqualTo(8);
        assertThat(cursor.chars).isEqualTo(new int[] {2, 2});

        assertThat(iterator.hasNext()).isFalse();
    }

    @Test
    public void testToJson_order1() {
        var table = new TransitionTable(1, " abc");

        assertThat(table.toJson()).isEqualTo(Json.createArrayBuilder().build());

        table.push('a');
        table.push('b');
        table.push('c');
        table.push('a');

        assertThat(table.toJson())
                .isEqualTo(
                        Json.createArrayBuilder()
                            .add(Json.createArrayBuilder().add(97).add(0.5))
                            .add(Json.createArrayBuilder().add(98).add(0.25))
                            .add(Json.createArrayBuilder().add(99).add(0.25))
                            .build());
    }

    @Test
    public void testToJson_order2() {
        var table = new TransitionTable(2, " abc");

        assertThat(table.toJson()).isEqualTo(Json.createArrayBuilder().build());

        table.push('a');
        table.push('b');
        table.push('c');
        table.push('a');
        table.push('b');

        assertThat(table.toJson())
                .isEqualTo(
                        Json.createArrayBuilder()
                            .add(Json.createArrayBuilder().add(32).add(97).add(0.2))
                            .add(Json.createArrayBuilder().add(97).add(98).add(0.4))
                            .add(Json.createArrayBuilder().add(98).add(99).add(0.2))
                            .add(Json.createArrayBuilder().add(99).add(97).add(0.2))
                            .build());
    }

    @Test
    public void testToJson_order3() {
        var table = new TransitionTable(3, " abc");

        assertThat(table.toJson()).isEqualTo(Json.createArrayBuilder().build());

        table.push('a');
        table.push('b');
        table.push('c');
        table.push('a');
        table.push('b');

        assertThat(table.toJson())
                .isEqualTo(
                        Json.createArrayBuilder()
                            .add(Json.createArrayBuilder().add(32).add(32).add(97).add(0.2))
                            .add(Json.createArrayBuilder().add(32).add(97).add(98).add(0.2))
                            .add(Json.createArrayBuilder().add(97).add(98).add(99).add(0.2))
                            .add(Json.createArrayBuilder().add(98).add(99).add(97).add(0.2))
                            .add(Json.createArrayBuilder().add(99).add(97).add(98).add(0.2))
                            .build());
    }
}
