package com.neo4jh3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;

public class TestHierarchy extends BaseTestH3Core{
    public void testH3ToParent() {
        assertEquals(0x801dfffffffffffL, h3.cellToParent(0x811d7ffffffffffL, 0));
        assertEquals(0x801dfffffffffffL, h3.cellToParent(0x801dfffffffffffL, 0));
        assertEquals(0x8828308281fffffL, h3.cellToParent(0x8928308280fffffL, 8));
        assertEquals(0x872830828ffffffL, h3.cellToParent(0x8928308280fffffL, 7));
        assertEquals("872830828ffffff", h3.cellToParentAddress("8928308280fffff", 7));
      }

      @Test(expected = IllegalArgumentException.class)
  public void testH3ToParentInvalidRes() {
    h3.cellToParent(0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testH3ToParentInvalid() {
    h3.cellToParent(0x8928308280fffffL, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testH3ToParentInvalid2() {
    h3.cellToParent(0x8928308280fffffL, 17);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testH3ToParentInvalid3() {
    h3.cellToParent(0, 17);
  }

  @Test
  public void testH3ToChildren() {
    List<String> sfChildren = h3.cellToChildren("88283082803ffff", 9);

    assertEquals(7, sfChildren.size());
    assertTrue(sfChildren.contains("8928308280fffff"));
    assertTrue(sfChildren.contains("8928308280bffff"));
    assertTrue(sfChildren.contains("8928308281bffff"));
    assertTrue(sfChildren.contains("89283082813ffff"));
    assertTrue(sfChildren.contains("89283082817ffff"));
    assertTrue(sfChildren.contains("89283082807ffff"));
    assertTrue(sfChildren.contains("89283082803ffff"));

    List<Long> pentagonChildren = h3.cellToChildren(0x801dfffffffffffL, 2);

    // res 0 pentagon has 5 hexagon children and 1 pentagon child at res 1.
    // Total output will be:
    //   5 * 7 children of res 1 hexagons
    //   6 children of res 1 pentagon
    assertEquals(5 * 7 + 6, pentagonChildren.size());

    // Don't crash
    h3.cellToChildren(0, 2);
    try {
      h3.cellToChildren("88283082803ffff", -1);
      assertTrue(false);
    } catch (IllegalArgumentException ex) {
      // expected
    }
    try {
      h3.cellToChildren("88283082803ffff", 17);
      assertTrue(false);
    } catch (IllegalArgumentException ex) {
      // expected
    }
  }

}
