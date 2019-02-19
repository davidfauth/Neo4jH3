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


    CALL com.dfauth.h3.returnHexAddress(38.439779, -77.410522,"9");
	CALL com.dfauth.h3.polygonSearch
	CALL com.dfauth.h3.lineBetweenLocations(38.418582, -77.385268,38.500603, -77.444288);
	
More examples can be found at: http://www.intelliwareness.org/2019/02/neo4j-uber-h3-geospatial/
