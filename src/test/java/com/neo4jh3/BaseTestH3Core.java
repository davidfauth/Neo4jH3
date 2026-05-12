package com.neo4jh3;

import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.uber.h3core.H3Core;

/** Base class for tests of the class {@link H3Core} */
public abstract class BaseTestH3Core {
  public static final double EPSILON = 1e-6;

  protected static H3Core h3;

  @BeforeAll
  public static void setup() throws IOException {
    h3 = H3Core.newInstance();
  }
}
