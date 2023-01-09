package com.neo4jh3;

import org.assertj.core.api.Assertions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

import com.neo4jh3.uber.Uberh3;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class Neo4jH3Test {
    private static Driver driver;
    private static Neo4j embeddedDatabaseServer;

   @Test
   public void should_return_hex_address() throws InterruptedException {
    embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
            .withDisabledServer()
            .withProcedure(Uberh3.class)
            .withFunction(Uberh3.class)
            .build();
      driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI());

    try (Session session = driver.session()) {
        Result result = session.run(
                "RETURN com.neo4jh3.h3HexAddress(37.563688, -122.324486, 3) AS value");
        assertEquals("832834fffffffff", result.single().get("value").asString());

        result = session.run("RETURN com.neo4jh3.cellToParent('892830926cfffff', 6) AS value");
        assertEquals("862830927ffffff", result.single().get("value").asString());

        result = session.run("RETURN com.neo4jh3.cellToParentNumber(604197150066212863, 3) AS value");
        assertEquals(590686371182542847L, result.single().get("value").asLong());

        result = session.run("return com.neo4jh3.h3RingsForDistance(6,7) as value");
        assertEquals(3, result.single().get("value").asInt());

        result = session.run("return com.neo4jh3.distanceBetweenHexes('8a2989352777fff','8a498935223ffff') as value");
        assertEquals(2360.8203881920604,result.single().get("value").asDouble(),0);

        result = session.run("return com.neo4jh3.gridDistance('85283473fffffff','8528342bfffffff') as value");
        assertEquals(2,result.single().get("value").asInt());

        //result = session.run("return com.neo4jh3.cellToLatLng('8a2989352717fff') as value");
        //assertEquals("39.678106484915,-120.23540752726865",result.single().get("value").asString());
     
     }
     driver.close();
     embeddedDatabaseServer.close();
    
  }
}
