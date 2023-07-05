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
        Result result = null;
        
        
        
        /* 
        result = session.run("call com.neo4jh3.compactNumber(['85283473fffffff','8528342bfffffff']) yield value return value limit 1");
        assertEquals(599686042433355775L,result.single().get(0).asLong());
        */

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
                assertEquals("\"37.81989535912348,-122.47829651373911\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.cellToLatLngString('892830926cfffff') AS value");
                assertEquals("\"37.56424780593243,-122.32530588312142\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.cellToLatLngString('123') AS value");
                assertEquals("\"-1\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.cellToLatLng(123) AS value");
                assertEquals("\"-1\"", result.single().get("value").toString());
                
                result = session.run("return com.neo4jh3.distanceBetweenHexes(599686042433355775,599686015589810175) as value");
                assertEquals(17.870163466857125,result.single().get("value").asDouble(),0);
        
                result = session.run("return com.neo4jh3.distanceBetweenHexes(3111,599686015589810175) as value");
                assertEquals(-1.0,result.single().get("value").asDouble(),0);
        
                result = session.run("return com.neo4jh3.distanceBetweenHexesString('8a2989352777fff','8a498935223ffff') as value");
                assertEquals(2360.8203881920604,result.single().get("value").asDouble(),0);
        
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

                result = session.run("call com.neo4jh3.multilineash3('MULTILINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',12) yield value return value limit 1");
                assertEquals(631243922688264703L, result.single().get(0).asLong(),0);
       
                result = session.run("call com.neo4jh3.multilineash3String('MULTILINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',12) yield value return value limit 1");
                assertEquals("\"8c2a100d27549ff\"",result.single().get(0).toString());
                
                result = session.run("call com.neo4jh3.multilineash3String('ZZZ((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',12) yield value return value limit 1");
                assertEquals("\"-1\"",result.single().get(0).toString());
                
                result = session.run("call com.neo4jh3.multilineash3String('MULTILINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',17) yield value return value limit 1");
                assertEquals("\"-2\"",result.single().get(0).toString());
                
                result = session.run("call com.neo4jh3.multilineash3('MULTILINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',17) yield value return value limit 1");
                assertEquals(-2L,result.single().get(0).asLong(),0);
                
                result = session.run("call com.neo4jh3.lineash3('LINESTRING((40.736691045913472 73.99311953429248), (40.73733046783797 -73.99265431029018) , (40.93733046783797 -74.00265431029018))',12) yield value return value limit 1");
                assertEquals(631243922688264703L, result.single().get(0).asLong(),0);
       
                /* Geography tests */
                result = session.run("RETURN com.neo4jh3.pointash3('POINT(37.8199 -122.4783)',13) AS value");
                assertEquals(635714569676958015L, result.single().get("value").asLong(),0);
       
                result = session.run("RETURN com.neo4jh3.centeraswkb(599686042433355775) AS value");
                assertEquals("\"00000000014042AC42F51330C6C05E7E7CF1A5AD49\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.boundaryaswkt(599686042433355775) AS value");
                assertEquals("\"POLYGON ((37.2713558667319 -121.91508032705622, 37.353926450852256 -121.86222328902491, 37.42834118609436 -121.92354999630156, 37.42012867767779 -122.03773496427027, 37.33755608435299 -122.090428929044, 37.26319797461824 -122.02910130919001, 37.2713558667319 -121.91508032705622))\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.centeraswkt(599686042433355775) AS value");
                assertEquals("\"POINT (37.34579337536847 -121.9763759725512)\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.centeraswktString('8009fffffffffff') AS value");
                assertEquals("\"POINT (64.70000012793487 10.53619907546767)\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.boundaryaswktString('8009fffffffffff') AS value");
                assertEquals("\"POLYGON ((63.095054077525454 -10.444977544778325, 55.70676846515226 5.523646549290313, 58.4015448703527 25.082722326707874, 68.92995788193983 31.83128049908738, 73.31022368544396 0.32561035194326043, 63.095054077525454 -10.444977544778325))\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.boundaryaswkb(599686042433355775) AS value");
                assertEquals("\"000000000300000001000000074042A2BBC9FE987BC05E7A90AD137AD84042AD4D7641CCC6C05E772EAA970D8A4042B6D3E24CE70EC05E7B1B717195834042B5C6C6C95E71C05E826A3FE95D384042AB3509AB6E53C05E85C9966B36CD4042A1B078A2ADECC05E81DCCBBCCF7B4042A2BBC9FE987BC05E7A90AD137AD8\"", result.single().get("value").toString());

                result = session.run("RETURN com.neo4jh3.boundaryaswkbString('8009fffffffffff') AS value");
                assertEquals("\"00000000030000000100000006404F8C2ABB652961C024E3D418C48DF8404BDA776399D62740161836CD0F75E9404D3365D283054D4039152D4A57DBFE40513B846E1065B2403FD4CECC7D6203405253DAB471DB4A3FD4D6CCCD35766F404F8C2ABB652961C024E3D418C48DF8\"", result.single().get("value").toString());
                
                result = session.run("RETURN com.neo4jh3.ispentagon(590112357393367039) AS value");
                assertEquals(true, result.single().get("value").asBoolean());
                
                result = session.run("RETURN com.neo4jh3.ispentagon(12345) AS value");
                assertEquals(false, result.single().get("value").asBoolean());
                
                result = session.run("RETURN com.neo4jh3.ispentagonString('85283473fffffff') AS value");
                assertEquals(false, result.single().get("value").asBoolean());
                
                result = session.run("RETURN com.neo4jh3.boundaryaswkbString('8009fffffffffff') AS value");
                assertEquals("\"00000000030000000100000006404F8C2ABB652961C024E3D418C48DF8404BDA776399D62740161836CD0F75E9404D3365D283054D4039152D4A57DBFE40513B846E1065B2403FD4CECC7D6203405253DAB471DB4A3FD4D6CCCD35766F404F8C2ABB652961C024E3D418C48DF8\"", result.single().get("value").toString());
                

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

     }
     driver.close();
     embeddedDatabaseServer.close();
    
  }
}
