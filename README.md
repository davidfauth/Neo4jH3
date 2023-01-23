# Neo4j H3 Library 
This library provides a set of H3 functions and procedures for Neo4j 5
H3 is a hexagonal hierarchical geospatial indexing system (https://h3geo.org)
This library uses the H3 api which can be found at https://h3geo.org/docs/api/indexing

Instructions
------------ 

This project uses maven, to build a jar-file with the procedure in this
project, simply package the project with maven:

    mvn clean package

This will produce a jar-file, `neo4jh3-0.9.1.jar `,
that can be copied to the `plugin` directory of your Neo4j instance.

    cp target/neo4jh3-0.9.1-SNAPSHOT.jar  neo4j-enterprise-5.x.0/plugins/.


Edit your Neo4j/conf/neo4j.conf file by adding this line:

    dbms.security.procedures.unrestricted=apoc.*,gds.*,com.neo4jh3.*
	dbms.security.procedures.allowlist=apoc.*,gds.*,com.neo4jh3.*
   
    
(Re)start Neo4j

# Functions
com.neo4jh3.uber.h3HexAddress(Double Lat, Double Long, Long Resolution) returns hexAddress;
com.neo4jh3.uber.h3HexAddressNumber(Double Lat, Double Long, Long Resolution) returns Long;
com.neo4jh3.uber.h3RingsForDistance(Double Resolution, Double Distance in KM) returns Long;
com.neo4jh3.gridDistance(String hexAddress, String hexAddress) returns int;
com.neo4jh3.gridDistanceNumber(Long hexAddress, Long hexAddress) returns int;
com.neo4jh3.cellToParent(String hexAddress, Long Resolution) returns String;
com.neo4jh3.cellToLatLng(String hexAddress) returns String;
com.neo4jh3.cellToLatLngNumber(Long hexAddress) returns String;
com.neo4jh3.distanceBetweenHexes(String hexAddress, String HexAddress) returns Long (km);
com.neo4jh3.distanceBetweenHexesNumber(String hexAddress, String HexAddress) returns Long (km);

    
# Procedures
com.neo4jh3.gridDisk(String hexAddress, Long ringSize) returns String;  
com.neo4jh3.cellToChildren(String hexAddress, Long resolution) returns String;  
com.neo4jh3.returnHexAddress(Double Lat, Double Long, Long Resolution) returns String hexAddress;  
com.neo4jh3.returnLongAddress(Double Lat, Double Long, Long Resolution) returns Long hexAddress;  
com.neo4jh3.polygonToCells(List String(lat,lon), List String(lat,lon), Long resolution) returns String;  
com.neo4jh3.gridPathCells(Double latitude, Double longitude, Double latitude, Double longitude, Long resolution) return string;  
com.neo4jh3.gridPathCellsHexAddress(String hexAddress, String hexAddress, Long resolution) returns String;  
com.neo4jh3.lineHexAddresses(List String(lat,lon), Long resolution) returns String;  

