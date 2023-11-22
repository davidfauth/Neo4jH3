# Neo4j H3 Library 
This library provides a set of H3 functions and procedures for Neo4j 5
H3 is a hexagonal hierarchical geospatial indexing system (https://h3geo.org)
This library uses the H3 api which can be found at https://h3geo.org/docs/api/indexing

Instructions
------------ 

This project uses maven, to build a jar-file with the procedure in this
project, simply package the project with maven:

    mvn clean package

This will produce a jar-file, `neo4jh3-5.13.0.jar`,
that can be copied to the `plugin` directory of your Neo4j instance.

    cp target/neo4jh3-5.13.0.jar  neo4j-enterprise-5.x.0/plugins/.


Edit your Neo4j/conf/neo4j.conf file by adding this line:

    dbms.security.procedures.unrestricted=apoc.*,gds.*,com.neo4jh3.*
	dbms.security.procedures.allowlist=apoc.*,gds.*,com.neo4jh3.*
   
    
(Re)start Neo4j

# Documentation
Refer to the Documentation.md file for detailed documenation on the functions / procedures.

# Note
The Neo4jH3 plugin requires the ability to write to the temp directory. If the temp directory configured with noexec, then you need to update the neo4j.conf with these two lines:
    server.jvm.additional=-Djava.io.tmpdir=/path_to_a_new_directory/temp
    server.jvm.additional=-Djna.tmpdir=/path_to_a_new_directory/temp

