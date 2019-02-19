package com.dfauth.schema;

import com.dfauth.results.StringResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Mode;
import org.neo4j.procedure.Procedure;

import java.io.IOException;
import java.util.stream.Stream;

public class Schema {

    @Context
    public GraphDatabaseService db;


    @Procedure(name="com.dfauth.schema.generate",mode= Mode.SCHEMA)
    @Description("CALL com.dfauth.schema.generate() - generate schema")

    public Stream<StringResult> generate() throws IOException {
        org.neo4j.graphdb.schema.Schema schema = db.schema();
        if (!schema.getConstraints(Labels.Provider).iterator().hasNext()) {
            schema.constraintFor(Labels.Provider)
                    .assertPropertyIsUnique("NPI")
                    .create();
        }

        if (!schema.getConstraints(Labels.Location).iterator().hasNext()) {
            schema.constraintFor(Labels.Location)
                    .assertPropertyIsUnique("LocationKey")
                    .create();
        }

        if (!schema.getConstraints(Labels.State).iterator().hasNext()) {
                schema.constraintFor(Labels.State)
                        .assertPropertyIsUnique("StateCode")
                        .create();
            }

        if(!schema.getIndexes(Labels.TaxonomyCode).iterator().hasNext()) {
            schema.indexFor(Labels.TaxonomyCode)
                    .on("code")
                    .create();
        }

        schema.indexFor(Labels.PostalCode)
                .on("PostalCode")
                .create();

        schema.indexFor(Labels.Location)
                .on("location")
                .create();

        schema.indexFor(Labels.County)
                .on("fipsCode")
                .create();

        schema.indexFor(Labels.Pharmacy)
                .on("name")
                .create();

        schema.indexFor(Labels.Location)
                .on("hexAddr")
                .create();

        return Stream.of(new StringResult("Schema Generated"));
    }

}
