package com.keybr.phoneticmodel;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class ChainTest {

    @Test
    public void toOffset() {
        var chain = new Chain(3, 4);

        assertThat(chain.spread).isEqualTo(64);

        assertThat(chain.toOffset()).isEqualTo(0);
        assertThat(chain.toArray()).isEqualTo(new int[] {0, 0, 0});

        assertThat(chain.push(0)).isFalse();
        assertThat(chain.toOffset()).isEqualTo(0);
        assertThat(chain.toArray()).isEqualTo(new int[] {0, 0, 0});

        assertThat(chain.push(1)).isTrue();
        assertThat(chain.toOffset()).isEqualTo(1);
        assertThat(chain.toArray()).isEqualTo(new int[] {0, 0, 1});

        assertThat(chain.push(0)).isTrue();
        assertThat(chain.toOffset()).isEqualTo(4);
        assertThat(chain.toArray()).isEqualTo(new int[] {0, 1, 0});

        assertThat(chain.push(0)).isFalse();
        assertThat(chain.toOffset()).isEqualTo(4);
        assertThat(chain.toArray()).isEqualTo(new int[] {0, 1, 0});

        assertThat(chain.push(2)).isTrue();
        assertThat(chain.toOffset()).isEqualTo(18);
        assertThat(chain.toArray()).isEqualTo(new int[] {1, 0, 2});

        assertThat(chain.push(3)).isTrue();
        assertThat(chain.toOffset()).isEqualTo(11);
        assertThat(chain.toArray()).isEqualTo(new int[] {0, 2, 3});
    }

    @Test
    public void fromOffset() {
        var chain = new Chain(3, 4);

        assertThat(chain.fromOffset(0)).isEqualTo(new int[] {0, 0, 0});
        assertThat(chain.fromOffset(1)).isEqualTo(new int[] {0, 0, 1});
        assertThat(chain.fromOffset(63)).isEqualTo(new int[] {3, 3, 3});
    }
}
