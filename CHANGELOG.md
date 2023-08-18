# CHANGELOG - Neo4jH3

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



