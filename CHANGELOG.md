# CHANGELOG - 5.16 - 2024-02-09
* [Updated] - Tested through Neo4j 5.16
* [Added] - Added two new procedures that write to the Neo4j database. These are com.neo4jh3.writeH3NodesRelsToDB and com.neo4jh3.writeH3StringNodesRelsToDB. These procedures take the from node, a list of hex addresses, a Label, a Property and a transaction size and write the H3 address nodes and create relationships between the H3 node and the From Node to the database. If you are using these procedures, it is highly recommended that you have a constraint or index for the Label and Property.
* [Added] - Added a function com.neo4jh3.angleBetweenPoints to calculate the angle between two points (latitude and longitude). 

# CHANGELOG - 5.15 - 2023-12-17
* [Updated] - Tested through Neo4j 5.15
* [Added] - Added MIT license.
* [Added] - Added two new procedures that write to the Neo4j database. These are com.neo4jh3.writeH3StringToDB and com.neo4jh3.writeH3ToDB. These procedures take a list of hex addresses, a Label, a Property and a transaction size and write the results to the database. If you are using these procedures, it is highly recommended that you have a constraint or index for the Label and Property.

# CHANGELOG - 5.14 - 2023-12-12
* [Updated] - Updated pom.xml to add multi-release plugin. There have been some changes to Neo4j 5.14 where Neo4j 5.14 supports both Java 17 and Java 21. The multi-release ensures that Neo4j will start with the plugin installed.

If you are using Neo4j 5.14, please use the 5.14 version of the Neo4jH3 plugin.

# CHANGELOG - 5.13 - 2023-11-20
* [ Added ] Tested through Neo4j 5.13
* [ Added ] Updated README.md to mention that /tmp directory needs to have execute privileges.
* [ Fixed ] Ensure the version value returned is correct.

## 5.12 2023-10-20
* [Added] Tested through Neo4j 5.12
* [Added] Added com.neo4jh3.polygonIntersection and com.neo4jh3.polygonIntersectionString to return the h3 addresses that are in the intersection between two polygons
* [Added] Added dependency on org.apache.commons.commons-math3 for decimal rounding
* [Updated] Updated com.neo4jh3.cellToLatLng, com.neo4jh3.cellToLatLngString, com.neo4jh3.centeraswkb, com.neo4jh3.centeraswkbString, com.neo4jh3.centeraswkt, com.neo4jh3.centeraswktString, com.neo4jh3.boundaryaswkt, com.neo4jh3.boundaryaswktString, com.neo4jh3.boundaryaswkb, com.neo4jh3.boundaryaswkbString, com.neo4jh3.centerasgeojson, com.neo4jh3.centerasgeojsonString, com.neo4jh3.boundaryasgeojson and com.neo4jh3.boundaryasgeojsonString to return results in lon / lat format at 6 decimal precision.
* [Updated] - Multiple fixes within the Documentation.md file



## 5.11 2023-08-18
* [Added] Tested through Neo4j 5.11
* [Updated] Updated the com.neo4jh3.pointash3String and com.neo4jh3.pointash3 functions to take a 3rd parameter to indicate if the point is in lat/lon format or lon/lat format.
* [Updated] Updated tests to address issues between Linux and Windows

## 5.10 2023-08-09

* [Added] Tested through Neo4j 5.10
* [Updated] Bumped org.apache.commons.commons-lang3 to 3.13.0

## 5.8 2023-05-19

* [Added] Tested through Neo4j 5.8
* [Added] Added support for converting a LINESTRING geospatial object to H3 number - com.neo4jh3.lineash3
* [Added] Added support for converting a LINESTRING geospatial object to H3 string - com.neo4jh3.lineash3String
* [Updated] Bumped H3 library to 4.1.1


## 5.7 2023-05-01

* [Added] Tested through Neo4j versions 5.7
* [Modified] Converted com.neo4jh3.multilineash3 from a function to a procedure. This procedure returns a list of H3 numbers that are along the line using the H3 gridpathcells feature.
* [Modified] Converted com.neo4jh3.multilineash3String from a function to a procedure. This procedure returns a list of H3 numbers that are along the line using the H3 gridpathcells feature.


## 5.5 2023-02-24

* [Added] Tested through Neo4j versions 5.5
* [Added] Added support for converting a MULTILINE geospatial object to H3 number - com.neo4jh3.multilineash3
* [Added] Added support for converting a MULTILINE geospatial object to H3 string - com.neo4jh3.multilineash3String



