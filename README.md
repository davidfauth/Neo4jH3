# Neo4jH3

Usage of Uber H3 with Neo4j
This project requires Neo4j 3.5.x or higher

Instructions
------------ 

This project uses maven, to build a jar-file with the procedure in this
project, simply package the project with maven:

    mvn clean package

This will produce a jar-file, `target/uberh3-0.1-SNAPSHOT.jar`,
that can be copied to the `plugin` directory of your Neo4j instance.

    cp target/uberh3-0.1-SNAPSHOT.jar neo4j-enterprise-3.5.2/plugins/.
    
Restart your Neo4j Server. Several new stored procedures are available:


    CALL com.dfauth.h3.returnHexAddress("38.439779", "-77.410522","9");
	CALL com.dfauth.h3.polygonSearch
	CALL call com.dfauth.h3.lineBetweenLocations(37.131,-71.234,38.13,-71.3)
