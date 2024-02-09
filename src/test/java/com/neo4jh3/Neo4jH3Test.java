package com.neo4jh3;

import org.assertj.core.api.Assertions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;

import com.neo4jh3.uber.Uberh3;
import com.uber.h3core.H3CoreLoader;


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
        Result result = null;
        
        
        
        /* 
        result = session.run("call com.neo4jh3.compactNumber(['85283473fffffff','8528342bfffffff']) yield value return value limit 1");
        assertEquals(599686042433355775L,result.single().get(0).asLong());
        */
                if (System.getProperty("os.name").toLowerCase().equalsIgnoreCase("mac os x")){
                        result = session.run("return com.neo4jh3.distanceBetweenHexes(599686042433355775,599686015589810175) as value");
                        assertEquals(17.870163, result.single().get("value").asDouble(),0);

                        result = session.run("RETURN com.neo4jh3.centeraswkb(599686042433355775) AS value");
                        assertEquals("\"0000000001C05E7E7CF1C3265B4042AC42F1ED17C6\"", result.single().get("value").toString());

                        result = session.run("RETURN com.neo4jh3.boundaryaswkt(599686042433355775) AS value");
                        assertEquals("\"POLYGON ((-121.91508 37.271356, -121.862223 37.353926, -121.92355 37.428341, -122.037735 37.420129, -122.090429 37.337556, -122.029101 37.263198, -121.91508 37.271356))\"", result.single().get("value").toString());
                
                        result = session.run("RETURN com.neo4jh3.boundaryaswkt(111) AS value");
                        assertEquals("\"-1\"", result.single().get("value").toString());
                        
                        result = session.run("RETURN com.neo4jh3.boundaryaswktString('822d57fffffffff') AS value");
                        assertEquals("\"POLYGON ((38.777546 44.198571, 39.938746 42.736298, 42.150674 42.631271, 43.258395 44.047542, 42.146575 45.539505, 39.897167 45.559577, 38.777546 44.198571))\"", result.single().get("value").toString());

                        result = session.run("RETURN com.neo4jh3.boundaryaswktString('111') AS value");
                        assertEquals("\"-1\"", result.single().get("value").toString());
                        
                        result = session.run("RETURN com.neo4jh3.centeraswkt(599686042433355775) AS value");
                        assertEquals("\"POINT (-121.976376 37.345793)\"", result.single().get("value").toString());

                        result = session.run("RETURN com.neo4jh3.boundaryaswkb(599686042433355775) AS value");
                        assertEquals("\"00000000030000000100000007C05E7A90ABB44E514042A2BBCB1CC964C05E772EA960B6FA4042AD4D72799A20C05E7B1B71758E224042B6D3E0BD449AC05E826A400FBA884042B5C6C97D8CF4C05E85C996B7670A4042AB3508F648C7C05E81DCCA70D1FA4042A1B078D92FB2C05E7A90ABB44E514042A2BBCB1CC964\"", result.single().get("value").toString());

                        result = session.run("RETURN com.neo4jh3.boundaryaswkbString('8009fffffffffff') AS value");
                        assertEquals("\"00000000030000000100000006C024E3D4280AE105404F8C2ABABEAD4F40161836EB4E9814404BDA775FB2EDFE4039152D44DCA8E3404D3365D3996FA8403FD4CEC41DD1A240513B846E8F29D43FD4D6CB5350092D405253DAB5C39BCCC024E3D4280AE105404F8C2ABABEAD4F\"", result.single().get("value").toString());
               
                        result = session.run("RETURN com.neo4jh3.centeraswktString('8009fffffffffff') AS value");
                        assertEquals("\"POINT (10.536199 64.7)\"", result.single().get("value").toString());
                
                        result = session.run("RETURN com.neo4jh3.boundaryaswkbString('8009fffffffffff') AS value");
                        assertEquals("\"00000000030000000100000006C024E3D4280AE105404F8C2ABABEAD4F40161836EB4E9814404BDA775FB2EDFE4039152D44DCA8E3404D3365D3996FA8403FD4CEC41DD1A240513B846E8F29D43FD4D6CB5350092D405253DAB5C39BCCC024E3D4280AE105404F8C2ABABEAD4F\"", result.single().get("value").toString());
                
                        result = session.run("RETURN com.neo4jh3.centerasgeojson(599686042433355775) AS value");
                        assertEquals("\"{\\\"type\\\":\\\"Point\\\",\\\"coordinates\\\":[-121.976376,37.345793]}\"", result.single().get("value").toString());
                        
                        result = session.run("RETURN com.neo4jh3.centerasgeojsonString('8009fffffffffff') AS value");
                        assertEquals("\"{\\\"type\\\":\\\"Point\\\",\\\"coordinates\\\":[10.536199,64.7]}\"", result.single().get("value").toString());
                
                        result = session.run("RETURN com.neo4jh3.centerasgeojsonString('1234') AS value");
                        assertEquals("\"-1\"", result.single().get("value").toString());
                
                        result = session.run("RETURN com.neo4jh3.centerasgeojson(1234) AS value");
                        assertEquals("\"-1\"", result.single().get("value").toString());
                }

                if (System.getProperty("os.name").toLowerCase().startsWith("wind")){
                        result = session.run("return com.neo4jh3.distanceBetweenHexes(599686042433355775,599686015589810175) as value");
                        assertEquals(17.870163466857125,result.single().get("value").asDouble(),0);

                        result = session.run("RETURN com.neo4jh3.centeraswkb(599686042433355775) AS value");
                        assertEquals("\"0000000001C05E7E7CF1C3265B4042AC42F1ED17C6\"", result.single().get("value").toString());
                        
                        result = session.run("RETURN com.neo4jh3.boundaryaswkt(599686042433355775) AS value");
                        assertEquals("\"POLYGON ((-121.91508 37.271356, -121.862223 37.353926, -121.92355 37.428341, -122.037735 37.420129, -122.090429 37.337556, -122.029101 37.263198, -121.91508 37.271356))\"", result.single().get("value").toString());
                        
                        result = session.run("RETURN com.neo4jh3.centeraswkt(599686042433355775) AS value");
                        assertEquals("\"POINT (-121.976376 37.345793)\"", result.single().get("value").toString());

                        result = session.run("RETURN com.neo4jh3.boundaryaswktString('8009fffffffffff') AS value");
                        assertEquals("\"POLYGON ((-10.444978 63.095054, 5.523647 55.706768, 25.082722 58.401545, 31.83128 68.929958, 0.32561 73.310224, -10.444978 63.095054))\"", result.single().get("value").toString());

                        result = session.run("RETURN com.neo4jh3.boundaryaswkb(599686042433355775) AS value");
                        assertEquals("\"00000000030000000100000007C05E7A90ABB44E514042A2BBCB1CC964C05E772EA960B6FA4042AD4D72799A20C05E7B1B71758E224042B6D3E0BD449AC05E826A400FBA884042B5C6C97D8CF4C05E85C996B7670A4042AB3508F648C7C05E81DCCA70D1FA4042A1B078D92FB2C05E7A90ABB44E514042A2BBCB1CC964\"", result.single().get("value").toString());

                        result = session.run("RETURN com.neo4jh3.boundaryaswkbString('8009fffffffffff') AS value");
                        assertEquals("\"00000000030000000100000006C024E3D4280AE105404F8C2ABABEAD4F40161836EB4E9814404BDA775FB2EDFE4039152D44DCA8E3404D3365D3996FA8403FD4CEC41DD1A240513B846E8F29D43FD4D6CB5350092D405253DAB5C39BCCC024E3D4280AE105404F8C2ABABEAD4F\"", result.single().get("value").toString());
               
                        result = session.run("RETURN com.neo4jh3.centeraswktString('8009fffffffffff') AS value");
                        assertEquals("\"POINT (10.536199 64.7)\"", result.single().get("value").toString());
                
                 }

                result = session.run("RETURN com.neo4jh3.version() AS value");
                assertEquals("\"5.16.0\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.cellToLatLngString('892830926cfffff') AS value");
                assertEquals("\"37.564248,-122.325306\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.cellToLatLng(599686042433355775) AS value");
                assertEquals("\"37.345793,-121.976376\"", result.single().get("value").toString());
               
                result = session.run("RETURN com.neo4jh3.h3Validate(599686042433355775) AS value");
                assertEquals(599686042433355775L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.h3ValidateString('85283473fffffff') AS value");
                assertEquals("\"85283473fffffff\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.h3ValidateString('zzz1234a') AS value");
                assertEquals("\"-1\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.h3Validate(337) AS value");
                assertEquals(-1L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.h3Validate(599686042433355775) AS value");
                assertEquals(599686042433355775L, result.single().get("value").asLong(),0);

                result = session.run("RETURN com.neo4jh3.h3Validate(371) AS value");
                assertEquals(-1L, result.single().get("value").asLong(),0);

                result = session.run("RETURN com.neo4jh3.h3ValidateString('85283473fffffff') AS value");
                assertEquals("\"85283473fffffff\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.h3ValidateString('notvalidstring') AS value");
                assertEquals("\"-1\"", result.single().get("value").toString());

                result = session.run("call com.neo4jh3.compactString(['85283473fffffff', '85283447fffffff', '8528347bfffffff', '85283463fffffff', '85283477fffffff', '8528340ffffffff', '8528340bfffffff', '85283457fffffff', '85283443fffffff', '8528344ffffffff', '852836b7fffffff', '8528346bfffffff', '8528346ffffffff', '85283467fffffff', '8528342bfffffff', '8528343bfffffff', '85283407fffffff', '85283403fffffff', '8528341bfffffff']) yield value return value limit 1");
                assertEquals("\"85283447fffffff\"",result.single().get(0).toString());
 
                result=session.run("call com.neo4jh3.uncompact([599686030622195711,599686015589810175,599686014516068351,599686034917163007,599686029548453887,599686032769679359,599686198125920255,599686023106002943,599686027400970239,599686013442326527,599686012368584703,599686018811035647,595182446027210751], 5) yield value return value limit 1");
                assertEquals(599686030622195711L,result.single().get(0).asLong());
                
                result=session.run("call com.neo4jh3.uncompact([599686030622195711,599686015589810175,599686014516068351,599686034917163007,599686029548453887,599686032769679359,599686198125920255,599686023106002943,599686027400970239,599686013442326527,599686012368584703,599686018811035647,595182446027210751], 1) yield value return value limit 1");
                assertEquals(-2L,result.single().get(0).asLong());
                
                result = session.run("call com.neo4jh3.uncompactString(['85283447fffffff','8528340ffffffff','8528340bfffffff','85283457fffffff','85283443fffffff','8528344ffffffff','852836b7fffffff','8528342bfffffff','8528343bfffffff','85283407fffffff','85283403fffffff','8528341bfffffff','8428347ffffffff'],5) yield value return value limit 1");
                assertEquals("\"85283447fffffff\"",result.single().get(0).toString());

                result = session.run("RETURN com.neo4jh3.h3HexAddressString( 37.8199, -122.4783, 13) AS value");
                assertEquals("\"8d283087022a93f\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.h3HexAddressString( 97.8199, -122.4783, 13) AS value");
                assertEquals("\"-3\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.h3HexAddressString( 37.8199, -122.4783, 16) AS value");
                assertEquals("\"-2\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.h3HexAddress( 37.8199, -122.4783, 13) AS value");
                assertEquals(635714569676958015L, result.single().get("value").asLong(),0);
                
                result = session.run("RETURN com.neo4jh3.h3HexAddress( 37.8199, -122.4783, 16) AS value");
                assertEquals(-2L, result.single().get("value").asLong(),0);
                
                result = session.run("RETURN com.neo4jh3.h3HexAddress( 37.8199, -222.4783, 13) AS value");
                assertEquals(-4L, result.single().get("value").asLong(),0);
                
                result = session.run("RETURN com.neo4jh3.h3tostring(599686042433355775) AS value");
                assertEquals("\"85283473fffffff\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.h3tostring(22) AS value");
                assertEquals("\"-1\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.stringToH3('85283473fffffff') AS value");
                assertEquals(599686042433355775L, result.single().get("value").asLong(),0);
                
                result = session.run("RETURN com.neo4jh3.stringToH3('invalidhex') AS value");
                assertEquals(-1L, result.single().get("value").asLong(),0);
                
                result = session.run("RETURN com.neo4jh3.h3ResolutionString('85283473fffffff') AS value");
                assertEquals(5L, result.single().get("value").asLong(),0);
                
                result = session.run("RETURN com.neo4jh3.h3ResolutionString('notavalidhex') AS value");
                assertEquals(-1L, result.single().get("value").asLong(),0);
                
                result = session.run("RETURN com.neo4jh3.h3Resolution(599686042433355775) AS value");
                assertEquals(5L, result.single().get("value").asLong(),0);

                result = session.run("RETURN com.neo4jh3.h3Resolution(337) AS value");
                assertEquals(-1L, result.single().get("value").asLong(),0);

                result = session.run("RETURN com.neo4jh3.latlongash3String( 37.8199, -122.4783, 13) AS value");
                assertEquals("\"8d283087022a93f\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.latlongash3String( 107.8199, -122.4783, 13) AS value");
                assertEquals("\"-3\"", result.single().get("value").toString());
               
                result = session.run("RETURN com.neo4jh3.latlongash3String( 17.8199, -222.4783, 13) AS value");
                assertEquals("\"-4\"", result.single().get("value").toString());
               
                result = session.run("RETURN com.neo4jh3.latlongash3String( 17.8199, -122.4783, 22) AS value");
                assertEquals("\"-2\"", result.single().get("value").toString());
               
                result = session.run("RETURN com.neo4jh3.latlongash3( 37.8199, -122.4783, 13) AS value");
                assertEquals(635714569676958015L, result.single().get("value").asLong(),0);
               
                result = session.run("RETURN com.neo4jh3.latlongash3( 107.8199, -122.4783, 13) AS value");
                assertEquals(-3L, result.single().get("value").asLong(),0);
               
                result = session.run("RETURN com.neo4jh3.latlongash3( 17.8199, -222.4783, 13) AS value");
                assertEquals(-4L, result.single().get("value").asLong(),0);
               
                result = session.run("RETURN com.neo4jh3.latlongash3( 17.8199, -122.4783, 22) AS value");
                assertEquals(-2L, result.single().get("value").asLong(),0);
               
                result = session.run("return com.neo4jh3.h3RingsForDistance(6,7) as value");
                assertEquals(3, result.single().get("value").asInt());

                result = session.run("return com.neo4jh3.gridDistanceString('85283473fffffff','8528342bfffffff') as value");
                assertEquals(2L,result.single().get("value").asLong(),0);
                
                result = session.run("return com.neo4jh3.gridDistance(599686030622195711,599686015589810175) as value");
                assertEquals(2L,result.single().get("value").asLong(),0);
        
                result = session.run("return com.neo4jh3.gridDistance(1234,599686015589810175) as value");
                assertEquals(-1L,result.single().get("value").asLong(),0);
        
                result =  session.run("RETURN com.neo4jh3.toparentString('892830926cfffff', 6) AS value");
                assertEquals("862830927ffffff", result.single().get("value").asString());

                result =  session.run("RETURN com.neo4jh3.toparentString('892830926cfffff', 26) AS value");
                assertEquals("-2", result.single().get("value").asString());

                result = session.run("RETURN com.neo4jh3.toparent(604197150066212863, 3) AS value");
                assertEquals(590686371182542847L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.toparent(604197150066212863, 17) AS value");
                assertEquals(-2L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.toparent(604197150066212863, 13) AS value");
                assertEquals(-2L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.toparent(12345, 13) AS value");
                assertEquals(-1L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.cellToLatLng( 635714569676958015) AS value");
                assertEquals("\"37.819895,-122.478297\"", result.single().get("value").toString());
                
                /* 
                result = session.run("RETURN com.neo4jh3.cellToLatLngString('892830926cfffff') AS value");
                assertEquals("\"37.56424780593244,-122.3253058831214\"", result.single().get("value").toString());
                */

                result = session.run("RETURN com.neo4jh3.cellToLatLngString('123') AS value");
                assertEquals("\"-1\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.cellToLatLng(123) AS value");
                assertEquals("\"-1\"", result.single().get("value").toString());
                        
                result = session.run("return com.neo4jh3.distanceBetweenHexes(3111,599686015589810175) as value");
                assertEquals(-1.0,result.single().get("value").asDouble(),0);
        
                result = session.run("return com.neo4jh3.distanceBetweenHexesString('8a2989352777fff','8a498935223ffff') as value");
                assertEquals(2360.820388,result.single().get("value").asDouble(),0);
        
                result = session.run("return com.neo4jh3.distanceBetweenHexesString('123','8a498935223ffff') as value");
                assertEquals(-1.0,result.single().get("value").asDouble(),0);
        
                //result = session.run("return com.neo4jh3.cellToLatLng('8a2989352717fff') as value");
        //assertEquals("39.678106484915,-120.23540752726865",result.single().get("value").asString());
                result = session.run("RETURN com.neo4jh3.maxChild(599686042433355775, 10) AS value");
                assertEquals(622204040416821247L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.maxChild(599686042433355775, 20) AS value");
                assertEquals(-2L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.maxChild(123, 10) AS value");
                assertEquals(-1L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.minChild(599686042433355775, 10) AS value");
                assertEquals(622204039496499199L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.minChild(599686042433355775, 23) AS value");
                assertEquals(-2L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.minChild(123, 10) AS value");
                assertEquals(-1L, result.single().get("value").asLong());

                result = session.run("RETURN com.neo4jh3.maxChildString('85283473fffffff', 10) AS value");
                assertEquals("\"8a2834736db7fff\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.maxChildString('123', 10) AS value");
                assertEquals("\"-1\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.maxChildString('85283473fffffff', 20) AS value");
                assertEquals("\"-2\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.minChildString('85283473fffffff', 10) AS value");
                assertEquals("\"8a2834700007fff\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.minChildString('123', 10) AS value");
                assertEquals("\"-1\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.minChildString('85283473fffffff', 20) AS value");
                assertEquals("\"-2\"", result.single().get("value").toString());

                
                result = session.run("RETURN com.neo4jh3.h3ResolutionString('85283473fffffff') AS value");
                assertEquals(5L, result.single().get("value").asLong(),0);

                result = session.run("RETURN com.neo4jh3.h3Resolution(599686042433355775) AS value");
                assertEquals(5L, result.single().get("value").asLong(),0);

                result = session.run("RETURN com.neo4jh3.angleBetweenPoints(40.123,-78.111,40.555,-78.910) AS value");
                assertEquals(305.607560, result.single().get("value").asDouble(),0);

                /* Procedures */

                result=session.run("call com.neo4jh3.gridDisk(599686042433355775,1) yield value return value limit 1");
                assertEquals(599686042433355775L,result.single().get(0).asLong());
               
                result=session.run("call com.neo4jh3.gridDisk(123,1) yield value return value limit 1");
                assertEquals(-1L,result.single().get(0).asLong());
               
                result=session.run("call com.neo4jh3.gridDisk(599686042433355775,-3) yield value return value limit 1");
                assertEquals(-2L,result.single().get(0).asLong());
               
                result=session.run("call com.neo4jh3.gridDiskString('85283473fffffff',1) yield value return value limit 1");
                assertEquals("\"85283473fffffff\"",result.single().get(0).toString());
               
                result=session.run("call com.neo4jh3.gridDiskString('85283473fffffff',-3) yield value return value limit 1");
                assertEquals("\"-2\"",result.single().get(0).toString());
               
                result=session.run("call com.neo4jh3.gridDiskString('12345',1) yield value return value limit 1");
                assertEquals("\"-1\"",result.single().get(0).toString());
               
                result=session.run("call com.neo4jh3.tochildren(12345,1) yield value return value limit 1");
                assertEquals(-1L,result.single().get(0).asLong());
               
                result=session.run("call com.neo4jh3.tochildren(599686042433355775,0) yield value return value limit 1");
                assertEquals(-2L,result.single().get(0).asLong());
               
                result=session.run("call com.neo4jh3.tochildren(599686042433355775,6) yield value return value limit 1");
                assertEquals(604189641121202175L,result.single().get(0).asLong());
               
                result=session.run("call com.neo4jh3.tochildrenString('12345',1) yield value return value limit 1");
                assertEquals("\"-1\"",result.single().get(0).toString());
               
                result=session.run("call com.neo4jh3.tochildrenString('85283473fffffff',0) yield value return value limit 1");
                assertEquals("\"-2\"",result.single().get(0).toString());
               
                result=session.run("call com.neo4jh3.tochildrenString('85283473fffffff',6) yield value return value limit 1");
                assertEquals("\"862834707ffffff\"",result.single().get(0).toString());
                
                result=session.run("call com.neo4jh3.polygonToCells(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value limit 1");
                assertEquals(608692971759468543L,result.single().get(0).asLong(),0);

                result=session.run("call com.neo4jh3.polygonToCells(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],20,'latlon') yield value return value limit 1");
                assertEquals(-2L,result.single().get(0).asLong(),0);
                
                result=session.run("call com.neo4jh3.polygonToCellsString(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value limit 1");
                assertEquals("\"872830866ffffff\"",result.single().get(0).toString());

                result=session.run("call com.neo4jh3.polygonToCellsString(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],20,'latlon') yield value return value limit 1");
                assertEquals("\"-2\"",result.single().get(0).toString());

                result=session.run("call com.neo4jh3.polygonIntersection(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],['37.9866,-123.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value limit 1");
                assertEquals(608692975685337087L, result.single().get(0).asLong(),0);

                result=session.run("call com.neo4jh3.polygonIntersectionString(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],['37.9866,-123.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value limit 1");
                assertEquals("\"872830950ffffff\"",result.single().get(0).toString());

                result = session.run("call com.neo4jh3.multilineash3('MULTILINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',12) yield value return value limit 1");
                assertEquals(631243922688264703L, result.single().get(0).asLong(),0);
       
                result = session.run("call com.neo4jh3.multilineash3String('MULTILINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',12) yield value return value limit 1");
                assertEquals("\"8c2a100d27549ff\"",result.single().get(0).toString());
                
                result = session.run("call com.neo4jh3.multilineash3('MULTILINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',17) yield value return value limit 1");
                assertEquals(-2L,result.single().get(0).asLong(),0);
                
                result = session.run("call com.neo4jh3.multilineash3String('ZZZ((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',12) yield value return value limit 1");
                assertEquals("\"-1\"",result.single().get(0).toString());
                
                result = session.run("call com.neo4jh3.multilineash3String('MULTILINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',17) yield value return value limit 1");
                assertEquals("\"-2\"",result.single().get(0).toString());

                result = session.run("call com.neo4jh3.lineash3String('ZZZ((37.271355 -121.915080), (37.353926 -121.862223))',7) yield value return value limit 1");
                assertEquals("\"-1\"", result.single().get("value").toString());

                result = session.run("call com.neo4jh3.lineash3String('LINESTRING((37.271355 -121.915080), (37.353926 -121.862223))',7)  yield value return value limit 1");
                assertEquals("\"87283409affffff\"",result.single().get(0).toString());
                
                result = session.run("call com.neo4jh3.lineash3('ZZZ((37.271355 -121.915080), (37.353926 -121.862223))',7) yield value return value limit 1");
                assertEquals(-1L, result.single().get(0).asLong(),0);
       
                result = session.run("call com.neo4jh3.lineash3('LINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',12) yield value return value limit 1");
                assertEquals(631243922688264703L, result.single().get(0).asLong(),0);
       
                /* Geography tests */
                result = session.run("RETURN com.neo4jh3.pointash3('POINT(37.8199 -122.4783)',13, 'latlon') AS value");
                assertEquals(635714569676958015L, result.single().get("value").asLong(),0);
        
                result = session.run("RETURN com.neo4jh3.pointash3('POINT(-122.4783 37.8199)',13, 'lonlat') AS value");
                assertEquals(635714569676958015L, result.single().get("value").asLong(),0);
        
                result = session.run("RETURN com.neo4jh3.pointash3('POINT(-122.4783 37.8199)',16, 'lonlat') AS value");
                assertEquals(-2L,result.single().get(0).asLong(),0);

                result = session.run("RETURN com.neo4jh3.pointash3String('POINT(37.8199 -122.4783)',13, 'latlon') AS value");
                assertEquals("\"8d283087022a93f\"", result.single().get("value").toString());
                 
                result = session.run("RETURN com.neo4jh3.pointash3String('POINT(-122.4783 37.8199)',13, 'lonlat') AS value");
                assertEquals("\"8d283087022a93f\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.pointash3String('POINT(-122.4783 37.8199)',16, 'lonlat') AS value");
                assertEquals("\"-2\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.ispentagon(590112357393367039) AS value");
                assertEquals(true, result.single().get("value").asBoolean());
                
                result = session.run("RETURN com.neo4jh3.ispentagon(12345) AS value");
                assertEquals(false, result.single().get("value").asBoolean());
                
                result = session.run("RETURN com.neo4jh3.ispentagonString('85283473fffffff') AS value");
                assertEquals(false, result.single().get("value").asBoolean());
                
                result = session.run("call com.neo4jh3.gridpathlatlon(37.8199, -122.4783, 47.8199, -122.5, 13) yield value return value limit 1");
                assertEquals(635714569676958015L, result.single().get("value").asLong(),0);
                
                result = session.run("call com.neo4jh3.gridpathlatlon(97.8199, -122.4783, 47.8199, -122.5, 13) yield value return value limit 1");
                assertEquals(-3L, result.single().get("value").asLong(),0);
                
                result = session.run("call com.neo4jh3.gridpathlatlon(37.8199, -122.4783, 47.8199, -122.5, 23) yield value return value limit 1");
                assertEquals(-2L, result.single().get("value").asLong(),0);
                
                result = session.run("call com.neo4jh3.gridpathlatlon(37.8199, -122.4783, 47.8199, -222.5, 13) yield value return value limit 1");
                assertEquals(-4L, result.single().get("value").asLong(),0);
                
                result = session.run("call com.neo4jh3.gridpathlatlonString(97.8199, -122.4783, 47.8199, -122.5, 13) yield value return value limit 1");
                assertEquals("\"-3\"", result.single().get("value").toString());
                
                result = session.run("call com.neo4jh3.gridpathlatlonString(37.8199, -122.4783, 47.8199, -122.5, 23) yield value return value limit 1");
                assertEquals("\"-2\"", result.single().get("value").toString());
                
                result = session.run("call com.neo4jh3.gridpathlatlonString(37.8199, -122.4783, 47.8199, -222.5, 13) yield value return value limit 1");
                assertEquals("\"-4\"", result.single().get("value").toString());
                
                result = session.run("call com.neo4jh3.gridpathlatlonString(37.8199, -122.4783, 47.8199, -122.5, 13) yield value return value limit 1");
                assertEquals("\"8d283087022a93f\"", result.single().get("value").toString());

                result=session.run("call com.neo4jh3.linepolyIntersection(['37.271355,-121.915080','37.353926,-121.862223','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798','37.271355,-121.915080'],[],'LINESTRING((37.271355 -121.915080), (37.353926 -121.862223))',7,'latlon') yield value return value limit 1");
                assertEquals(608693241537101823L, result.single().get(0).asLong(),0);

                result=session.run("call com.neo4jh3.linepolyIntersection(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],'LINESTRING((37.271355 -121.915080), (37.353926 -121.862223))',27,'latlon') yield value return value limit 1");
                assertEquals(-2L, result.single().get(0).asLong(),0);

     }
     driver.close();
     embeddedDatabaseServer.close();
    
  }
}
