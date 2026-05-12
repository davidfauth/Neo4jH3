package com.neo4jh3;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class H3HierarchyTest extends BaseTestH3Core {

    @Test
    public void testH3ToParent() {
        assertThat(h3.cellToParent(0x811d7ffffffffffL, 0)).isEqualTo(0x801dfffffffffffL);
        assertThat(h3.cellToParent(0x801dfffffffffffL, 0)).isEqualTo(0x801dfffffffffffL);
        assertThat(h3.cellToParent(0x8928308280fffffL, 8)).isEqualTo(0x8828308281fffffL);
        assertThat(h3.cellToParent(0x8928308280fffffL, 7)).isEqualTo(0x872830828ffffffL);
        assertThat(h3.cellToParentAddress("8928308280fffff", 7)).isEqualTo("872830828ffffff");
    }

    @Test
    public void testH3ToParentInvalidRes() {
        assertThatThrownBy(() -> h3.cellToParent(0, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testH3ToParentInvalid() {
        assertThatThrownBy(() -> h3.cellToParent(0x8928308280fffffL, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testH3ToParentInvalid2() {
        assertThatThrownBy(() -> h3.cellToParent(0x8928308280fffffL, 17))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testH3ToParentInvalid3() {
        assertThatThrownBy(() -> h3.cellToParent(0, 17))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testH3ToChildren() {
        List<String> sfChildren = h3.cellToChildren("88283082803ffff", 9);

        assertThat(sfChildren).hasSize(7);
        assertThat(sfChildren).contains(
                "8928308280fffff",
                "8928308280bffff",
                "8928308281bffff",
                "89283082813ffff",
                "89283082817ffff",
                "89283082807ffff",
                "89283082803ffff"
        );

        List<Long> pentagonChildren = h3.cellToChildren(0x801dfffffffffffL, 2);
        assertThat(pentagonChildren).hasSize(5 * 7 + 6);

        // Don't crash
        h3.cellToChildren(0, 2);

        assertThatThrownBy(() -> h3.cellToChildren("88283082803ffff", -1))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> h3.cellToChildren("88283082803ffff", 17))
                .isInstanceOf(IllegalArgumentException.class);
    }
}