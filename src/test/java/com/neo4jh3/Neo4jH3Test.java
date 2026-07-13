package com.neo4jh3;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;

import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

import com.neo4jh3.uber.Neo4jH3;

/**
 * Integration tests for Neo4jH3 functions and procedures.
 *
 * Improvements over the original:
 *  - @BeforeAll / @AfterAll replace per-test setup/teardown (faster, avoids leaks)
 *  - Split into @Nested classes so failures are easier to locate
 *  - OS-specific geometry tests isolated in their own nested class
 *  - Missing coverage added: areNeighborCells false case, resolution 0, boundary
 *    conditions for lat/lon, directedEdge round-trip, distanceBetweenHexesString
 *    (the previously buggy double-check of fromHexAddress), angleBetweenPoints
 *    edge cases, gridDistance invalid inputs, and more
 *  - result variable declared inside each assertion block rather than reused
 *  - Removed duplicate assertions (h3ResolutionString / h3Resolution tested twice)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Neo4jH3Test {

    private Driver driver;
    private Neo4j embeddedDatabaseServer;

    // ── Known valid H3 cells used across tests ───────────────────────────────
    private static final long   CELL_SF_RES5       = 599686042433355775L;  // 85283473fffffff
    private static final long   CELL_SF_RES5_B     = 599686015589810175L;  // 85283477fffffff
    private static final long   CELL_SF_RES8       = 613177664827580415L;
    private static final long   CELL_SF_RES8_B     = 613196848737525759L;
    private static final long   CELL_SF_RES13      = 635714569676958015L;  // 8d283087022a93f
    private static final long   CELL_PENTAGON      = 590112357393367039L;
    private static final long   DIRECTED_EDGE      = 1536434764926418943L; // 115283473fffffff
    private static final String STR_CELL_SF_RES5   = "85283473fffffff";
    private static final String STR_CELL_SF_RES5_B = "85283477fffffff";
    private static final String STR_CELL_SF_RES8   = "8828308281fffff";
    private static final String STR_CELL_SF_RES8_B = "882830828bfffff";
    private static final String STR_DIRECTED_EDGE  = "115283473fffffff";

    @BeforeAll
    void startDb() {
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .withProcedure(Neo4jH3.class)
                .withFunction(Neo4jH3.class)
                .build();
        driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI());
    }

    @AfterAll
    void stopDb() {
        if (driver != null) driver.close();
        if (embeddedDatabaseServer != null) embeddedDatabaseServer.close();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Version
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class VersionTest {
        @Test
        void version() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.version() AS v").single().get("v").asString())
                        .isEqualTo("2026.06.0");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Validation
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class ValidationTest {

        @Test
        void h3Validate_validCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3Validate(" + CELL_SF_RES5 + ") AS v")
                        .single().get("v").asLong()).isEqualTo(CELL_SF_RES5);
            }
        }

        @Test
        void h3Validate_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3Validate(337) AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
                assertThat(session.run("RETURN neo4jh3.h3Validate(371) AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
            }
        }

        @Test
        void h3ValidateString_validCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3ValidateString('" + STR_CELL_SF_RES5 + "') AS v")
                        .single().get("v").asString()).isEqualTo(STR_CELL_SF_RES5);
            }
        }

        @Test
        void h3ValidateString_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3ValidateString('zzz1234a') AS v")
                        .single().get("v").asString()).isEqualTo("-1");
                assertThat(session.run("RETURN neo4jh3.h3ValidateString('notvalidstring') AS v")
                        .single().get("v").asString()).isEqualTo("-1");
            }
        }

        @Test
        void h3Resolution_validCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3Resolution(" + CELL_SF_RES5 + ") AS v")
                        .single().get("v").asLong()).isEqualTo(5L);
            }
        }

        @Test
        void h3Resolution_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3Resolution(337) AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
            }
        }

        @Test
        void h3ResolutionString_validCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3ResolutionString('" + STR_CELL_SF_RES5 + "') AS v")
                        .single().get("v").asLong()).isEqualTo(5L);
            }
        }

        @Test
        void h3ResolutionString_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3ResolutionString('notavalidhex') AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
            }
        }

        @Test
        void isPentagon_true() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.ispentagon(" + CELL_PENTAGON + ") AS v")
                        .single().get("v").asBoolean()).isTrue();
            }
        }

        @Test
        void isPentagon_false() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.ispentagon(12345) AS v")
                        .single().get("v").asBoolean()).isFalse();
                assertThat(session.run("RETURN neo4jh3.ispentagonString('" + STR_CELL_SF_RES5 + "') AS v")
                        .single().get("v").asBoolean()).isFalse();
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Coordinate conversion
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class CoordinateConversionTest {

        @Test
        void h3HexAddress_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3HexAddress(37.8199, -122.4783, 13) AS v")
                        .single().get("v").asLong()).isEqualTo(CELL_SF_RES13);
            }
        }

        @Test
        void h3HexAddress_resolution0_isValid() {
            // Resolution 0 is valid in H3 — previously rejected by the > 0 bug
            try (Session session = driver.session()) {
                long result = session.run("RETURN neo4jh3.h3HexAddress(37.8199, -122.4783, 0) AS v")
                        .single().get("v").asLong();
                assertThat(result).isGreaterThan(0L);
            }
        }

        @Test
        void h3HexAddress_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3HexAddress(37.8199, -122.4783, 16) AS v")
                        .single().get("v").asLong()).isEqualTo(-2L);
            }
        }

        @Test
        void h3HexAddress_latOutOfRange() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3HexAddress(97.8199, -122.4783, 13) AS v")
                        .single().get("v").asLong()).isEqualTo(-3L);
            }
        }

        @Test
        void h3HexAddress_lonOutOfRange() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3HexAddress(37.8199, -222.4783, 13) AS v")
                        .single().get("v").asLong()).isEqualTo(-4L);
            }
        }

        @Test
        void h3HexAddressString_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3HexAddressString(37.8199, -122.4783, 13) AS v")
                        .single().get("v").asString()).isEqualTo("8d283087022a93f");
            }
        }

        @Test
        void h3HexAddressString_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3HexAddressString(37.8199, -122.4783, 16) AS v")
                        .single().get("v").asString()).isEqualTo("-2");
            }
        }

        @Test
        void h3HexAddressString_latOutOfRange() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3HexAddressString(97.8199, -122.4783, 13) AS v")
                        .single().get("v").asString()).isEqualTo("-3");
            }
        }

        @Test
        void latlongash3_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.latlongash3(37.8199, -122.4783, 13) AS v")
                        .single().get("v").asLong()).isEqualTo(CELL_SF_RES13);
                assertThat(session.run("RETURN neo4jh3.latlongash3String(37.8199, -122.4783, 13) AS v")
                        .single().get("v").asString()).isEqualTo("8d283087022a93f");
            }
        }

        @Test
        void latlongash3_latOutOfRange() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.latlongash3(107.8199, -122.4783, 13) AS v")
                        .single().get("v").asLong()).isEqualTo(-3L);
                assertThat(session.run("RETURN neo4jh3.latlongash3String(107.8199, -122.4783, 13) AS v")
                        .single().get("v").asString()).isEqualTo("-3");
            }
        }

        @Test
        void latlongash3_lonOutOfRange() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.latlongash3(17.8199, -222.4783, 13) AS v")
                        .single().get("v").asLong()).isEqualTo(-4L);
                assertThat(session.run("RETURN neo4jh3.latlongash3String(17.8199, -222.4783, 13) AS v")
                        .single().get("v").asString()).isEqualTo("-4");
            }
        }

        @Test
        void latlongash3_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.latlongash3(17.8199, -122.4783, 22) AS v")
                        .single().get("v").asLong()).isEqualTo(-2L);
                assertThat(session.run("RETURN neo4jh3.latlongash3String(17.8199, -122.4783, 22) AS v")
                        .single().get("v").asString()).isEqualTo("-2");
            }
        }

        @Test
        void cellToLatLng_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.cellToLatLng(" + CELL_SF_RES13 + ") AS v")
                        .single().get("v").asString()).isEqualTo("37.819895,-122.478297");
                assertThat(session.run("RETURN neo4jh3.cellToLatLngString('892830926cfffff') AS v")
                        .single().get("v").asString()).isEqualTo("37.564248,-122.325306");
            }
        }

        @Test
        void cellToLatLng_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.cellToLatLng(123) AS v")
                        .single().get("v").asString()).isEqualTo("-1");
                assertThat(session.run("RETURN neo4jh3.cellToLatLngString('123') AS v")
                        .single().get("v").asString()).isEqualTo("-1");
            }
        }

        @Test
        void h3tostring_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3tostring(" + CELL_SF_RES5 + ") AS v")
                        .single().get("v").asString()).isEqualTo(STR_CELL_SF_RES5);
            }
        }

        @Test
        void h3tostring_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3tostring(22) AS v")
                        .single().get("v").asString()).isEqualTo("-1");
            }
        }

        @Test
        void stringToH3_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.stringToH3('" + STR_CELL_SF_RES5 + "') AS v")
                        .single().get("v").asLong()).isEqualTo(CELL_SF_RES5);
            }
        }

        @Test
        void stringToH3_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.stringToH3('invalidhex') AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
            }
        }

        // NEW: boundary lat/lon values (exactly ±90 / ±180 should be valid)
        @Test
        void h3HexAddress_boundaryLatLon() {
            try (Session session = driver.session()) {
                long result = session.run("RETURN neo4jh3.h3HexAddress(90.0, 0.0, 5) AS v")
                        .single().get("v").asLong();
                assertThat(result).isGreaterThan(0L);

                result = session.run("RETURN neo4jh3.h3HexAddress(-90.0, 0.0, 5) AS v")
                        .single().get("v").asLong();
                assertThat(result).isGreaterThan(0L);

                result = session.run("RETURN neo4jh3.h3HexAddress(0.0, 180.0, 5) AS v")
                        .single().get("v").asLong();
                assertThat(result).isGreaterThan(0L);
            }
        }

        // NEW: angleBetweenPoints — exercises the fixed lat validation bug
        @Test
        void angleBetweenPoints_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.angleBetweenPoints(40.123,-78.111,40.555,-78.910) AS v")
                        .single().get("v").asDouble()).isEqualTo(305.607560);
            }
        }

        @Test
        void angleBetweenPoints_lat1OutOfRange() {
            // Previously the broken check (lat1>90 || lat2<-90) meant lat1=-91
            // would NOT have been caught; this verifies the fix
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.angleBetweenPoints(91.0,-78.111,40.555,-78.910) AS v")
                        .single().get("v").asDouble()).isEqualTo(-1.0);
                assertThat(session.run("RETURN neo4jh3.angleBetweenPoints(-91.0,-78.111,40.555,-78.910) AS v")
                        .single().get("v").asDouble()).isEqualTo(-1.0);
            }
        }

        @Test
        void angleBetweenPoints_lat2OutOfRange() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.angleBetweenPoints(40.123,-78.111,91.0,-78.910) AS v")
                        .single().get("v").asDouble()).isEqualTo(-1.0);
            }
        }

        @Test
        void angleBetweenPoints_lonOutOfRange() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.angleBetweenPoints(40.123,181.0,40.555,-78.910) AS v")
                        .single().get("v").asDouble()).isEqualTo(-2.0);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Hierarchy: parent / children
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class HierarchyTest {

        @Test
        void toparent_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.toparent(604197150066212863, 3) AS v")
                        .single().get("v").asLong()).isEqualTo(590686371182542847L);
                assertThat(session.run("RETURN neo4jh3.toparentString('892830926cfffff', 6) AS v")
                        .single().get("v").asString()).isEqualTo("862830927ffffff");
            }
        }

        @Test
        void toparent_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.toparent(604197150066212863, 17) AS v")
                        .single().get("v").asLong()).isEqualTo(-2L);
                assertThat(session.run("RETURN neo4jh3.toparent(604197150066212863, 13) AS v")
                        .single().get("v").asLong()).isEqualTo(-2L);
                assertThat(session.run("RETURN neo4jh3.toparentString('892830926cfffff', 26) AS v")
                        .single().get("v").asString()).isEqualTo("-2");
            }
        }

        @Test
        void toparent_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.toparent(12345, 13) AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
            }
        }

        @Test
        void tochildren_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.tochildren(" + CELL_SF_RES5 + ",6) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(604189641121202175L);
                assertThat(session.run("call neo4jh3.tochildrenString('" + STR_CELL_SF_RES5 + "',6) yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("862834707ffffff");
            }
        }

        @Test
        void tochildren_resolutionTooLow() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.tochildren(" + CELL_SF_RES5 + ",0) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-2L);
                assertThat(session.run("call neo4jh3.tochildrenString('" + STR_CELL_SF_RES5 + "',0) yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("-2");
            }
        }

        @Test
        void tochildren_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.tochildren(12345,1) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-1L);
                assertThat(session.run("call neo4jh3.tochildrenString('12345',1) yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("-1");
            }
        }

        @Test
        void maxChild_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.maxChild(" + CELL_SF_RES5 + ", 10) AS v")
                        .single().get("v").asLong()).isEqualTo(622204040416821247L);
                assertThat(session.run("RETURN neo4jh3.maxChildString('" + STR_CELL_SF_RES5 + "', 10) AS v")
                        .single().get("v").asString()).isEqualTo("8a2834736db7fff");
            }
        }

        @Test
        void maxChild_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.maxChild(" + CELL_SF_RES5 + ", 20) AS v")
                        .single().get("v").asLong()).isEqualTo(-2L);
                assertThat(session.run("RETURN neo4jh3.maxChildString('" + STR_CELL_SF_RES5 + "', 20) AS v")
                        .single().get("v").asString()).isEqualTo("-2");
            }
        }

        @Test
        void maxChild_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.maxChild(123, 10) AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
                assertThat(session.run("RETURN neo4jh3.maxChildString('123', 10) AS v")
                        .single().get("v").asString()).isEqualTo("-1");
            }
        }

        @Test
        void minChild_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.minChild(" + CELL_SF_RES5 + ", 10) AS v")
                        .single().get("v").asLong()).isEqualTo(622204039496499199L);
                assertThat(session.run("RETURN neo4jh3.minChildString('" + STR_CELL_SF_RES5 + "', 10) AS v")
                        .single().get("v").asString()).isEqualTo("8a2834700007fff");
            }
        }

        @Test
        void minChild_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.minChild(" + CELL_SF_RES5 + ", 23) AS v")
                        .single().get("v").asLong()).isEqualTo(-2L);
                assertThat(session.run("RETURN neo4jh3.minChildString('" + STR_CELL_SF_RES5 + "', 20) AS v")
                        .single().get("v").asString()).isEqualTo("-2");
            }
        }

        @Test
        void minChild_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.minChild(123, 10) AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
                assertThat(session.run("RETURN neo4jh3.minChildString('123', 10) AS v")
                        .single().get("v").asString()).isEqualTo("-1");
            }
        }

        // NEW: minChild <= maxChild sanity check
        @Test
        void minChild_lessThanOrEqualToMaxChild() {
            try (Session session = driver.session()) {
                long min = session.run("RETURN neo4jh3.minChild(" + CELL_SF_RES5 + ", 10) AS v")
                        .single().get("v").asLong();
                long max = session.run("RETURN neo4jh3.maxChild(" + CELL_SF_RES5 + ", 10) AS v")
                        .single().get("v").asLong();
                assertThat(min).isLessThanOrEqualTo(max);
            }
        }

        // NEW: parent of child round-trip
        @Test
        void parentOfChild_roundTrip() {
            try (Session session = driver.session()) {
                long child = session.run("call neo4jh3.tochildren(" + CELL_SF_RES5 + ",6) yield value return value limit 1")
                        .single().get(0).asLong();
                long parent = session.run("RETURN neo4jh3.toparent(" + child + ", 5) AS v")
                        .single().get("v").asLong();
                assertThat(parent).isEqualTo(CELL_SF_RES5);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Distance & rings
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class DistanceTest {

        @Test
        void gridDistance_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.gridDistance(599686030622195711,599686015589810175) AS v")
                        .single().get("v").asLong()).isEqualTo(2L);
                assertThat(session.run("RETURN neo4jh3.gridDistanceString('85283473fffffff','8528342bfffffff') AS v")
                        .single().get("v").asLong()).isEqualTo(2L);
            }
        }

        @Test
        void gridDistance_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.gridDistance(1234," + CELL_SF_RES5_B + ") AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
            }
        }

        // NEW: both cells invalid
        @Test
        void gridDistance_bothCellsInvalid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.gridDistance(1234, 5678) AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
            }
        }

        // NEW: same cell distance = 0
        @Test
        void gridDistance_sameCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.gridDistance(" + CELL_SF_RES5 + "," + CELL_SF_RES5 + ") AS v")
                        .single().get("v").asLong()).isEqualTo(0L);
            }
        }

        @Test
        void distanceBetweenHexes_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.distanceBetweenHexes(3111," + CELL_SF_RES5_B + ") AS v")
                        .single().get("v").asDouble()).isEqualTo(-1.0);
            }
        }

        // NEW: verifies the bug fix — was checking isValidCell(fromHexAddress) twice
        @Test
        void distanceBetweenHexesString_toAddressInvalidReturnsMinusOne() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.distanceBetweenHexesString('123','" + STR_CELL_SF_RES8_B + "') AS v")
                        .single().get("v").asDouble()).isEqualTo(-1.0);
                assertThat(session.run("RETURN neo4jh3.distanceBetweenHexesString('" + STR_CELL_SF_RES8 + "','1234') AS v")
                        .single().get("v").asDouble()).isEqualTo(-1.0);
            }
        }

        @Test
        void distanceBetweenHexesString_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.distanceBetweenHexesString('" + STR_CELL_SF_RES8 + "','" + STR_CELL_SF_RES8_B + "') AS v")
                        .single().get("v").asDouble()).isEqualTo(0.95163);
            }
        }

        @Test
        void h3RingsForDistance() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3RingsForDistance(6,7) AS v")
                        .single().get("v").asInt()).isEqualTo(2);
            }
        }

        // NEW: rings for distance 0 should return 0
        @Test
        void h3RingsForDistance_zero() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.h3RingsForDistance(9,0) AS v")
                        .single().get("v").asLong()).isEqualTo(0L);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Neighbors
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class NeighborTest {

        @Test
        void areNeighborCells_trueCase_long() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.areNeighborCells(" + CELL_SF_RES5 + "," + CELL_SF_RES5_B + ") AS v")
                        .single().get("v").asLong()).isEqualTo(1L);
            }
        }

        @Test
        void areNeighborCells_falseCase_long() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.areNeighborCells(611163894932570111," + CELL_SF_RES5 + ") AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
            }
        }

        @Test
        void areNeighborCellsString_trueCase() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.areNeighborCellsString('" + STR_CELL_SF_RES5 + "','" + STR_CELL_SF_RES5_B + "') AS v")
                        .single().get("v").asString()).isEqualTo("1");
            }
        }

        @Test
        void areNeighborCellsString_differentResolutions() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.areNeighborCellsString('892830926cfffff','" + STR_CELL_SF_RES5_B + "') AS v")
                        .single().get("v").asString()).isEqualTo("-1");
            }
        }

        // NEW: valid same-resolution cells that are NOT adjacent return "0"
        @Test
        void areNeighborCellsString_notAdjacent() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.areNeighborCellsString('85283473fffffff','8528342bfffffff') AS v")
                        .single().get("v").asString()).isEqualTo("0");
            }
        }

        @Test
        void gridDisk_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.gridDisk(" + CELL_SF_RES5 + ",1) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(CELL_SF_RES5);
                assertThat(session.run("call neo4jh3.gridDiskString('" + STR_CELL_SF_RES5 + "',1) yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo(STR_CELL_SF_RES5);
            }
        }

        @Test
        void gridDisk_negativeRingSize() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.gridDisk(" + CELL_SF_RES5 + ",-3) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-2L);
                assertThat(session.run("call neo4jh3.gridDiskString('" + STR_CELL_SF_RES5 + "',-3) yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("-2");
            }
        }

        @Test
        void gridDisk_invalidCell() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.gridDisk(123,1) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-1L);
                assertThat(session.run("call neo4jh3.gridDiskString('12345',1) yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("-1");
            }
        }

        // NEW: ring size 0 returns exactly 1 cell
        @Test
        void gridDisk_ringZeroReturnsSingleCell() {
            try (Session session = driver.session()) {
                long count = session.run("call neo4jh3.gridDisk(" + CELL_SF_RES8 + ",0) yield value return count(value)")
                        .single().get(0).asLong();
                assertThat(count).isEqualTo(1L);
            }
        }

        // NEW: ring size 1 returns 7 cells (center + 6 neighbors)
        @Test
        void gridDisk_ring1ReturnsSeven() {
            try (Session session = driver.session()) {
                long count = session.run("call neo4jh3.gridDisk(" + CELL_SF_RES5 + ",1) yield value return count(value)")
                        .single().get(0).asLong();
                assertThat(count).isEqualTo(7L);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Directed edges
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class DirectedEdgeTest {

        @Test
        void isValidDirectedEdge_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.isValidDirectedEdge(" + DIRECTED_EDGE + ") AS v")
                        .single().get("v").asLong()).isEqualTo(1L);
                assertThat(session.run("RETURN neo4jh3.isValidDirectedEdgeString('" + STR_DIRECTED_EDGE + "') AS v")
                        .single().get("v").asString()).isEqualTo("1");
            }
        }

        @Test
        void isValidDirectedEdge_invalidEdge() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.isValidDirectedEdge(611163894932570111) AS v")
                        .single().get("v").asLong()).isEqualTo(0L);
            }
        }

        @Test
        void getDirectedEdgeOrigin() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.getDirectedEdgeOrigin(" + DIRECTED_EDGE + ") AS v")
                        .single().get("v").asLong()).isEqualTo(CELL_SF_RES5);
                assertThat(session.run("RETURN neo4jh3.getDirectedEdgeOriginString('" + STR_DIRECTED_EDGE + "') AS v")
                        .single().get("v").asString()).isEqualTo(STR_CELL_SF_RES5);
            }
        }

        @Test
        void getDirectedEdgeDestination() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.getDirectedEdgeDestination(" + DIRECTED_EDGE + ") AS v")
                        .single().get("v").asLong()).isEqualTo(CELL_SF_RES5_B);
                assertThat(session.run("RETURN neo4jh3.getDirectedEdgeDestinationString('" + STR_DIRECTED_EDGE + "') AS v")
                        .single().get("v").asString()).isEqualTo(STR_CELL_SF_RES5_B);
            }
        }

        @Test
        void getDirectedEdge_invalidEdge() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.getDirectedEdgeOrigin(999) AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
                assertThat(session.run("RETURN neo4jh3.getDirectedEdgeDestination(999) AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
            }
        }

        @Test
        void cellsToDirectedEdge_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.cellsToDirectedEdge(" + CELL_SF_RES5 + "," + CELL_SF_RES5_B + ") yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(DIRECTED_EDGE);
                assertThat(session.run("call neo4jh3.cellsToDirectedEdgeString('" + STR_CELL_SF_RES5 + "','" + STR_CELL_SF_RES5_B + "') yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo(STR_DIRECTED_EDGE);
            }
        }

        @Test
        void reverseDirectedEdge_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.reverseDirectedEdge(" + DIRECTED_EDGE + ") AS v")
                        .single().get("v").asLong()).isEqualTo(1320261955969089535L);
                assertThat(session.run("RETURN neo4jh3.reverseDirectedEdgeString('" + STR_DIRECTED_EDGE + "') AS v")
                        .single().get("v").asString()).isEqualTo("165283477fffffff");
            }
        }

        @Test
        void reverseDirectedEdge_invalidEdge() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.reverseDirectedEdge(" + CELL_SF_RES5 + ") AS v")
                        .single().get("v").asLong()).isEqualTo(-1L);
                assertThat(session.run("RETURN neo4jh3.reverseDirectedEdgeString('" + STR_CELL_SF_RES5 + "') AS v")
                        .single().get("v").asString()).isEqualTo("-1");
            }
        }

        // NEW: double-reverse round-trip
        @Test
        void reverseDirectedEdge_doubleReverseRoundTrip() {
            try (Session session = driver.session()) {
                long reversed = session.run("RETURN neo4jh3.reverseDirectedEdge(" + DIRECTED_EDGE + ") AS v")
                        .single().get("v").asLong();
                long doubleReversed = session.run("RETURN neo4jh3.reverseDirectedEdge(" + reversed + ") AS v")
                        .single().get("v").asLong();
                assertThat(doubleReversed).isEqualTo(DIRECTED_EDGE);
            }
        }

        // NEW: origin/destination are swapped after reversing
        @Test
        void reverseDirectedEdge_originDestinationSwapped() {
            try (Session session = driver.session()) {
                long reversed = session.run("RETURN neo4jh3.reverseDirectedEdge(" + DIRECTED_EDGE + ") AS v")
                        .single().get("v").asLong();
                long originOfReversed = session.run("RETURN neo4jh3.getDirectedEdgeOrigin(" + reversed + ") AS v")
                        .single().get("v").asLong();
                long destOfReversed = session.run("RETURN neo4jh3.getDirectedEdgeDestination(" + reversed + ") AS v")
                        .single().get("v").asLong();
                assertThat(originOfReversed).isEqualTo(CELL_SF_RES5_B);
                assertThat(destOfReversed).isEqualTo(CELL_SF_RES5);
            }
        }

        @Test
        void originToDirectedEdges_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.originToDirectedEdges(" + CELL_SF_RES5_B + ") yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(1248204361931161599L);
                assertThat(session.run("call neo4jh3.originToDirectedEdgesString('" + STR_CELL_SF_RES5 + "') yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo(STR_DIRECTED_EDGE);
            }
        }

        // NEW: non-pentagon always has exactly 6 directed edges
        @Test
        void originToDirectedEdges_hexagonHasSixEdges() {
            try (Session session = driver.session()) {
                long count = session.run("call neo4jh3.originToDirectedEdges(" + CELL_SF_RES5 + ") yield value return count(value)")
                        .single().get(0).asLong();
                assertThat(count).isEqualTo(6L);
            }
        }

        @Test
        void directedEdgeToBoundary_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.directedEdgeToBoundaryString('" + STR_DIRECTED_EDGE + "') yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("37.420129,-122.037735");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Compact / Uncompact
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class CompactTest {

        @Test
        void compactString_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.compactString(['85283473fffffff', '85283447fffffff', '8528347bfffffff', '85283463fffffff', '85283477fffffff', '8528340ffffffff', '8528340bfffffff', '85283457fffffff', '85283443fffffff', '8528344ffffffff', '852836b7fffffff', '8528346bfffffff', '8528346ffffffff', '85283467fffffff', '8528342bfffffff', '8528343bfffffff', '85283407fffffff', '85283403fffffff', '8528341bfffffff']) yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("85283447fffffff");
            }
        }

        @Test
        void uncompact_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.uncompact([599686030622195711,599686015589810175,599686014516068351,599686034917163007,599686029548453887,599686032769679359,599686198125920255,599686023106002943,599686027400970239,599686013442326527,599686012368584703,599686018811035647,595182446027210751], 5) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(599686030622195711L);
            }
        }

        @Test
        void uncompact_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.uncompact([599686030622195711,599686015589810175,599686014516068351,599686034917163007,599686029548453887,599686032769679359,599686198125920255,599686023106002943,599686027400970239,599686013442326527,599686012368584703,599686018811035647,595182446027210751], 1) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-2L);
            }
        }

        @Test
        void uncompactString_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.uncompactString(['85283447fffffff','8528340ffffffff','8528340bfffffff','85283457fffffff','85283443fffffff','8528344ffffffff','852836b7fffffff','8528342bfffffff','8528343bfffffff','85283407fffffff','85283403fffffff','8528341bfffffff','8428347ffffffff'],5) yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("85283447fffffff");
            }
        }

        // NEW: 7 res-6 children of a res-5 cell compact back to 1 cell
        @Test
        void compactUncompact_roundTrip() {
            try (Session session = driver.session()) {
                long compactedCount = session.run(
                        "call neo4jh3.tochildren(" + CELL_SF_RES5 + ",6) yield value as cell " +
                        "with collect(cell) as cells " +
                        "call neo4jh3.compact(cells) yield value " +
                        "return count(value)")
                        .single().get(0).asLong();
                assertThat(compactedCount).isEqualTo(1L);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Grid path
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class GridPathTest {

        @Test
        void gridpathlatlon_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.gridpathlatlon(37.8199, -122.4783, 47.8199, -122.5, 13) yield value return value limit 1")
                        .single().get("value").asLong()).isEqualTo(CELL_SF_RES13);
                assertThat(session.run("call neo4jh3.gridpathlatlonString(37.8199, -122.4783, 47.8199, -122.5, 13) yield value return value limit 1")
                        .single().get("value").asString()).isEqualTo("8d283087022a93f");
            }
        }

        @Test
        void gridpathlatlon_latOutOfRange() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.gridpathlatlon(97.8199, -122.4783, 47.8199, -122.5, 13) yield value return value limit 1")
                        .single().get("value").asLong()).isEqualTo(-3L);
                assertThat(session.run("call neo4jh3.gridpathlatlonString(97.8199, -122.4783, 47.8199, -122.5, 13) yield value return value limit 1")
                        .single().get("value").asString()).isEqualTo("-3");
            }
        }

        @Test
        void gridpathlatlon_lonOutOfRange() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.gridpathlatlon(37.8199, -122.4783, 47.8199, -222.5, 13) yield value return value limit 1")
                        .single().get("value").asLong()).isEqualTo(-4L);
                assertThat(session.run("call neo4jh3.gridpathlatlonString(37.8199, -122.4783, 47.8199, -222.5, 13) yield value return value limit 1")
                        .single().get("value").asString()).isEqualTo("-4");
            }
        }

        @Test
        void gridpathlatlon_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.gridpathlatlon(37.8199, -122.4783, 47.8199, -122.5, 23) yield value return value limit 1")
                        .single().get("value").asLong()).isEqualTo(-2L);
                assertThat(session.run("call neo4jh3.gridpathlatlonString(37.8199, -122.4783, 47.8199, -122.5, 23) yield value return value limit 1")
                        .single().get("value").asString()).isEqualTo("-2");
            }
        }

        // NEW: path from a cell to itself returns exactly 1 cell
        @Test
        void gridpathlatlon_sameStartAndEnd() {
            try (Session session = driver.session()) {
                long count = session.run("call neo4jh3.gridpathlatlon(37.8199, -122.4783, 37.8199, -122.4783, 13) yield value return count(value)")
                        .single().get(0).asLong();
                assertThat(count).isEqualTo(1L);
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Geometry (WKT / WKB / GeoJSON / line / polygon)
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class GeometryTest {

        @Test
        void pointash3_latlon() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.pointash3('POINT(37.8199 -122.4783)',13,'latlon') AS v")
                        .single().get("v").asLong()).isEqualTo(CELL_SF_RES13);
                assertThat(session.run("RETURN neo4jh3.pointash3String('POINT(37.8199 -122.4783)',13,'latlon') AS v")
                        .single().get("v").asString()).isEqualTo("8d283087022a93f");
            }
        }

        @Test
        void pointash3_lonlat() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.pointash3('POINT(-122.4783 37.8199)',13,'lonlat') AS v")
                        .single().get("v").asLong()).isEqualTo(CELL_SF_RES13);
            }
        }

        @Test
        void pointash3_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.pointash3('POINT(-122.4783 37.8199)',16,'lonlat') AS v")
                        .single().get(0).asLong()).isEqualTo(-2L);
                assertThat(session.run("RETURN neo4jh3.pointash3String('POINT(-122.4783 37.8199)',16,'lonlat') AS v")
                        .single().get("v").asString()).isEqualTo("-2");
            }
        }

        @Test
        void polygonash3_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.polygonash3('POLYGON((-77.436031 38.471420, -77.395123 38.536569, -77.294124 38.511703, -77.310611 38.395709, -77.436031 38.471420))',13) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(635758222702368959L);
            }
        }

        @Test
        void polygonash3_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.polygonash3('POLYGON((-77.436031 38.471420, -77.395123 38.536569, -77.294124 38.511703, -77.310611 38.395709, -77.436031 38.471420))',19) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-2L);
            }
        }

        @Test
        void polygonash3String_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.polygonash3String('POLYGON((-77.436031 38.471420, -77.395123 38.536569, -77.294124 38.511703, -77.310611 38.395709, -77.436031 38.471420))',13) yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("8d2aabc333558bf");
            }
        }

        @Test
        void polygonToCells_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.polygonToCells(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(608692971759468543L);
                assertThat(session.run("call neo4jh3.polygonToCellsString(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("872830866ffffff");
            }
        }

        @Test
        void polygonToCells_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.polygonToCells(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],20,'latlon') yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-2L);
            }
        }

        @Test
        void polygonIntersection_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.polygonIntersection(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],['37.9866,-123.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(608692975685337087L);
                assertThat(session.run("call neo4jh3.polygonIntersectionString(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],['37.9866,-123.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("872830950ffffff");
            }
        }

        // NEW: non-overlapping polygons return empty result
        @Test
        void polygonIntersection_noOverlap() {
            try (Session session = driver.session()) {
                long count = session.run("call neo4jh3.polygonIntersection(" +
                        "['40.7128,-74.0060','40.7580,-73.9855','40.7489,-73.9680','40.7128,-74.0060'],[]," +
                        "['37.7749,-122.4194','37.8044,-122.2712','37.7041,-122.1319','37.7749,-122.4194'],[]," +
                        "7,'latlon') yield value return count(value)")
                        .single().get(0).asLong();
                assertThat(count).isEqualTo(0L);
            }
        }

        @Test
        void multipolygonash3_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.multipolygonash3('MULTIPOLYGON(((-73.927881 40.769855, -73.915189 40.763915,-73.923600 40.753839, -73.944151 40.759309, -73.927881 40.769855)),((-77.436031 38.471420, -77.395123 38.536569, -77.294124 38.511703, -77.310611 38.395709, -77.436031 38.471420)))',8) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(613240230178193407L);
            }
        }

        @Test
        void multipolygonash3_count() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.multipolygonash3('MULTIPOLYGON(((-73.927881 40.769855, -73.915189 40.763915,-73.923600 40.753839, -73.944151 40.759309, -73.927881 40.769855)),((-77.436031 38.471420, -77.395123 38.536569, -77.294124 38.511703, -77.310611 38.395709, -77.436031 38.471420)))',8) yield value return count(value)")
                        .single().get(0).asLong()).isEqualTo(162L);
            }
        }

        @Test
        void multipolygonash3_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.multipolygonash3('MULTIPOLYGON(((-73.927881 40.769855, -73.915189 40.763915,-73.923600 40.753839, -73.944151 40.759309, -73.927881 40.769855)),((-77.436031 38.471420, -77.395123 38.536569, -77.294124 38.511703, -77.310611 38.395709, -77.436031 38.471420)))',19) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-2L);
            }
        }

        @Test
        void geojsonmultipolygonash3_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.geojsonmultipolygonash3([[-73.927881, 40.769855], [-73.915189, 40.763915], [-73.923600, 40.753839], [-73.944151, 40.759309], [-73.927881, 40.769855]],8) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(613229524731035647L);
            }
        }

        @Test
        void geojsonmultipolygonash3_invalidInput() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.geojsonmultipolygonash3([[123456.0, 111]],7) yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-1L);
            }
        }

        @Test
        void lineash3_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.lineash3('LINESTRING((73.99311953429248 40.736691045913472), (-73.99265431029018 40.73733046783797), (-74.00265431029018 40.93733046783797))',12,'lonlat') yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(631243922688264703L);
                assertThat(session.run("call neo4jh3.lineash3String('LINESTRING((-121.915080 37.271355 ), (-121.862223 37.353926))',7,'lonlat') yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("87283409affffff");
            }
        }

        @Test
        void lineash3_invalidPrefix() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.lineash3('ZZZ((-121.915080 37.271355), (-121.862223 37.353926))',7,'lonlat') yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-1L);
                assertThat(session.run("call neo4jh3.lineash3String('ZZZ((-121.915080 37.271355 ), (-121.862223 37.353926))',7,'lonlat') yield value return value limit 1")
                        .single().get("value").asString()).isEqualTo("-1");
            }
        }

        @Test
        void multilineash3_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.multilineash3('MULTILINESTRING((-73.99311 40.73669), (-73.99265 40.73733), (-74.00265 40.93733))',12,'lonlat') yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(631243922688246783L);
                assertThat(session.run("call neo4jh3.multilineash3String('MULTILINESTRING((-73.99311 40.73669), (-73.99265 40.73733), (-74.00265 40.93733))',12,'lonlat') yield value return value order by value limit 1")
                        .single().get(0).asString()).isEqualTo("8c2a100800925ff");
            }
        }

        @Test
        void multilineash3_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.multilineash3('MULTILINESTRING((-73.99311953429248 40.736691045913472), (-73.99265431029018 40.73733046783797), ( -74.00265431029018 40.93733046783797))',17,'lonlat') yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-2L);
            }
        }

        @Test
        void multilineash3_invalidPrefix() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.multilineash3String('ZZZ((-73.99311953429248 40.736691045913472), (-73.99265431029018 40.73733046783797), ( -74.00265431029018 40.93733046783797))',12,'lonlat') yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("-1");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Coverage
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class CoverageTest {

        @Test
        void coverage_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.coverage(['-122.481889,37.826683','122.479487,37.808548','-122.481889,37.826683','-122.479487,37.808548','-122.474150,37.808904','-122.476510,37.826935','-122.481889,37.826683'],8,'lonlat') yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(613196571542028287L);
                assertThat(session.run("call neo4jh3.coverageString(['-122.481889,37.826683','122.479487,37.808548','-122.481889,37.826683','-122.479487,37.808548','-122.474150,37.808904','-122.476510,37.826935','-122.481889,37.826683'],8,'lonlat') yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("8828308703fffff");
            }
        }

        @Test
        void coverage_invalidResolution() {
            try (Session session = driver.session()) {
                assertThat(session.run("call neo4jh3.coverage(['-122.481889,37.826683','122.479487,37.808548','-122.481889,37.826683','-122.479487,37.808548','-122.474150,37.808904','-122.476510,37.826935','-122.481889,37.826683'],17,'lonlat') yield value return value limit 1")
                        .single().get(0).asLong()).isEqualTo(-2L);
                assertThat(session.run("call neo4jh3.coverageString(['-122.481889,37.826683','122.479487,37.808548','-122.481889,37.826683','-122.479487,37.808548','-122.474150,37.808904','-122.476510,37.826935','-122.481889,37.826683'],17,'lonlat') yield value return value limit 1")
                        .single().get(0).asString()).isEqualTo("-2");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Vertex
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class VertexTest {

        @Test
        void vertexLatLng_valid() {
            try (Session session = driver.session()) {
                assertThat(session.run("RETURN neo4jh3.vertexLatLngString('" + STR_CELL_SF_RES5 + "') AS v")
                        .single().get("v").asString()).isEqualTo("37.271356 -121.91508");
                assertThat(session.run("RETURN neo4jh3.vertexLatLng(611163894932570111) AS v")
                        .single().get("v").asString()).isEqualTo("-26.440265 -151.876706");
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  OS-specific geometry (floating-point output varies by platform)
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    class OsSpecificGeometryTest {

        @Test
        void geometryTests() {
            String os = System.getProperty("os.name").toLowerCase();

            try (Session session = driver.session()) {
                if (os.equalsIgnoreCase("mac os x")) {
                    assertThat(session.run("RETURN neo4jh3.distanceBetweenHexes(" + CELL_SF_RES5 + "," + CELL_SF_RES5_B + ") AS v")
                            .single().get("v").asDouble()).isEqualTo(17.870163);
                    assertThat(session.run("RETURN neo4jh3.centeraswkb(" + CELL_SF_RES5 + ") AS v")
                            .single().get("v").asString()).isEqualTo("0000000001C05E7E7CF1C3265B4042AC42F1ED17C6");
                    assertThat(session.run("RETURN neo4jh3.boundaryaswkt(" + CELL_SF_RES5 + ") AS v")
                            .single().get("v").asString()).isEqualTo("POLYGON ((-121.91508 37.271356, -121.862223 37.353926, -121.92355 37.428341, -122.037735 37.420129, -122.090429 37.337556, -122.029101 37.263198, -121.91508 37.271356))");
                    assertThat(session.run("RETURN neo4jh3.centeraswkt(" + CELL_SF_RES5 + ") AS v")
                            .single().get("v").asString()).isEqualTo("POINT (-121.976376 37.345793)");
                    assertThat(session.run("RETURN neo4jh3.centerasgeojson(" + CELL_SF_RES5 + ") AS v")
                            .single().get("v").asString()).isEqualTo("{\"type\":\"Point\",\"coordinates\":[-121.976376,37.345793]}");
                    assertThat(session.run("RETURN neo4jh3.centerasgeojsonString('8009fffffffffff') AS v")
                            .single().get("v").asString()).isEqualTo("{\"type\":\"Point\",\"coordinates\":[10.536199,64.7]}");
                }

                if (os.startsWith("wind")) {
                    assertThat(session.run("RETURN neo4jh3.distanceBetweenHexes(" + CELL_SF_RES5 + "," + CELL_SF_RES5_B + ") AS v")
                            .single().get("v").asDouble()).isEqualTo(17.870163466857125);
                    assertThat(session.run("RETURN neo4jh3.centeraswkb(" + CELL_SF_RES5 + ") AS v")
                            .single().get("v").asString()).isEqualTo("0000000001C05E7E7CF1C3265B4042AC42F1ED17C6");
                    assertThat(session.run("RETURN neo4jh3.boundaryaswkt(" + CELL_SF_RES5 + ") AS v")
                            .single().get("v").asString()).isEqualTo("POLYGON ((-121.91508 37.271356, -121.862223 37.353926, -121.92355 37.428341, -122.037735 37.420129, -122.090429 37.337556, -122.029101 37.263198, -121.91508 37.271356))");
                    assertThat(session.run("RETURN neo4jh3.centeraswkt(" + CELL_SF_RES5 + ") AS v")
                            .single().get("v").asString()).isEqualTo("POINT (-121.976376 37.345793)");
                }

                // Shared across all platforms
                assertThat(session.run("RETURN neo4jh3.boundaryaswkt(111) AS v")
                        .single().get("v").asString()).isEqualTo("-1");
                assertThat(session.run("RETURN neo4jh3.centerasgeojson(1234) AS v")
                        .single().get("v").asString()).isEqualTo("-1");
                assertThat(session.run("RETURN neo4jh3.centerasgeojsonString('1234') AS v")
                        .single().get("v").asString()).isEqualTo("-1");
            }
        }
    }
}