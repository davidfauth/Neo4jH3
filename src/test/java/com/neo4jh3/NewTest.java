package com.neo4jh3;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

import com.neo4jh3.uber.Neo4jH3;

import static org.assertj.core.api.Assertions.assertThat;

import com.neo4jh3.uber.Neo4jH3;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NewTest {
    private Neo4j embeddedDatabaseServer;

    @BeforeAll
    void initializeNeo4j() {
        this.embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .withProcedure(Neo4jH3.class)
                .withFunction(Neo4jH3.class)
                .build();
    }

    @AfterAll
    void closeNeo4j() {
        this.embeddedDatabaseServer.close();
    }
    @Test
    void returnVersion() {
        Result result = null;
         try(Driver driver = GraphDatabase.driver(embeddedDatabaseServer.boltURI());
            Session session = driver.session()) {
            result = session.run("RETURN neo4jh3.version() AS value");
            assertThat(result.single().get("value").toString()).isEqualTo("\"2026.04.0\""); 
            }
    }
}
