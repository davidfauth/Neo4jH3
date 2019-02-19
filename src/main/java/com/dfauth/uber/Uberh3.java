package com.dfauth.uber;

import com.dfauth.results.NodeResult;
import com.dfauth.results.NodeListResult;
import com.dfauth.results.StringResult;
import com.uber.h3core.*;
import com.uber.h3core.util.*;
import com.uber.h3core.util.GeoCoord;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.helpers.collection.Iterators;

import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.stream.Stream;
import java.util.*;
import java.util.Map.Entry;

public class Uberh3 {
    @Context
    public GraphDatabaseService db;

    @Context
    public Log log;

    @Procedure(name = "com.dfauth.h3.returnHexAddress", mode = Mode.WRITE)
    @Description("CALL com.dfauth.h3.returnHexAddress(latitude, longitude, h3Res)")
    public Stream<StringResult> h3HexAddress(@Name ("locLatitude") Double locLatitude, @Name("locLongitude") Double locLongitude, @Name("h3Res") String h3Res) throws InterruptedException {
        String hexAddr = null;
            
        try {
			int h3Resolution = 9;
            H3Core h3 = H3Core.newInstance();
            int thisH3Resolution = Integer.parseInt(h3Res);
            if (thisH3Resolution > 0 && thisH3Resolution <=15) {
                h3Resolution = thisH3Resolution;
				hexAddr = h3.geoToH3Address(locLatitude, locLongitude, h3Resolution);
			}

        }    catch (Exception e) {
                System.out.println(e);
            }  finally {
     //           tx1.close();
            }

        return Stream.of(new StringResult(hexAddr));
    }
	

    @Procedure(name = "com.dfauth.h3.hexArea", mode = Mode.WRITE)
    @Description("CALL com.dfauth.h3.hexArea(hexSize)")
    public Stream<StringResult> h3RingSize(@Name("hexSize") String hexSize) throws InterruptedException {
        Double hexArea = 0.0;
        try {
            H3Core h3 = H3Core.newInstance();
            hexArea = h3.hexArea(Integer.parseInt(hexSize),AreaUnit.km2);


        }    catch (Exception e) {
                System.out.println(e);
            }  finally {
     //           tx1.close();
            }

        return Stream.of(new StringResult("Hex size is " + Double.toString(hexArea) + " square meters"));
    }

    @Procedure(name = "com.dfauth.h3.locationsByRingSize", mode = Mode.WRITE)
    @Description("CALL com.dfauth.h3.locationsByRingSizes(hexAddress, ringSize)")
        public Stream<NodeListResult> findNearbyHex(@Name("hexAddress") String hexAddress, @Name("ringSize") String ringSize) throws InterruptedException {
            Map<Node, String> map = new HashMap<Node,String>();
            Map<Node, List<Node>> results = new HashMap<>();
            
            Node node = null;
            Relationship rel = null;

            try {
                H3Core h3 = H3Core.newInstance();
                
                List<String> ringList = null;
            
                ringList = h3.kRing(hexAddress, Integer.parseInt(ringSize));
                Iterator<String> ringListIterator = ringList.iterator();


                Result result = db.execute( "UNWIND $hexs AS hexaddress MATCH (l:Location {hexAddr:hexaddress}) RETURN l;", Collections.singletonMap( "hexs", ringList ) );
                Iterator<Node> n_column = result.columnAs( "l" );
                
                List<Node> reasons;
                for ( Node nodeIter : Iterators.asIterable( n_column ) )
                {
                    reasons = new ArrayList<>();
                    reasons.add(nodeIter);
                    map.put(nodeIter,nodeIter.getProperty( "hexAddr" ).toString());
                   results.put(nodeIter,reasons);
                 }
                 
      //          tx1.close();
                
            }    catch (Exception e) {
                System.out.println(e);
            }  finally {
     //           tx1.close();
            }

            // return a node and its linked nodes
            return results.entrySet().stream().map(entry -> {
                entry.getValue().add(entry.getKey());
                return new NodeListResult(entry.getValue());
            });
        }


        @Procedure(name = "com.dfauth.h3.locationsLatLongByRingSize", mode = Mode.WRITE)
        @Description("CALL com.dfauth.h3.locationsLatLongByRingSize(latitude, longitude, ringSize)")
            public Stream<NodeListResult> findNearbyLatLong(@Name("startLat") Double startLat,@Name("startLong") Double startLong, @Name("ringSize") String ringSize) throws InterruptedException {
                Map<Node, String> map = new HashMap<Node,String>();
                Map<Node, List<Node>> results = new HashMap<>();
                
                Node node = null;
                Relationship rel = null;
                String hexAddr = null;
    
                try {
                    H3Core h3 = H3Core.newInstance();
                    
                    List<String> ringList = null;
                    hexAddr = h3.geoToH3Address(startLat, startLong, 9);
                    System.out.println("Hex Address is: " + hexAddr);
                    ringList = h3.kRing(hexAddr, Integer.parseInt(ringSize));
                    Iterator<String> ringListIterator = ringList.iterator();

    
                    Result result = db.execute( "UNWIND $hexs AS hexaddress MATCH (l:Location {hexAddr:hexaddress}) RETURN l;", Collections.singletonMap( "hexs", ringList ) );
                    Iterator<Node> n_column = result.columnAs( "l" );
                    
                    List<Node> reasons;
                    for ( Node nodeIter : Iterators.asIterable( n_column ) )
                    {
                        reasons = new ArrayList<>();
                        reasons.add(nodeIter);
                        map.put(nodeIter,nodeIter.getProperty( "hexAddr" ).toString());
                       results.put(nodeIter,reasons);
                     }
                     
          //          tx1.close();
                    
                }    catch (Exception e) {
                    System.out.println(e);
                }  finally {
         //           tx1.close();
                }
    
                // return a node and its linked nodes
                return results.entrySet().stream().map(entry -> {
                    entry.getValue().add(entry.getKey());
                    return new NodeListResult(entry.getValue());
                });
            } 
            
            
            @Procedure(name = "com.dfauth.h3.polygonSearch", mode = Mode.WRITE)
            @Description("CALL com.dfauth.h3.polygonSearch(polyEdges)")
                public Stream<NodeListResult> findNodesPolygon(@Name("polyEdges") List<Map<String, Object>> polyEdges,@Name("polyEdgeHoles") List<Map<String, Object>> polyEdgeHoles) throws InterruptedException {
                    Map<Node, String> map = new HashMap<Node,String>();
                    Map<Node, List<Node>> results = new HashMap<>();
                    
                    List<GeoCoord> hexPoints = new ArrayList();
                    List<GeoCoord> hexHoles = new ArrayList();
                    List<List<GeoCoord>> holesList = new ArrayList<List<GeoCoord>>();

                    Node node = null;
                    Relationship rel = null;
                    String hexAddr = null;
                    Double thisLat = 0.0;
                    Double thisLong = 0.0;
                    
                    try {
                        
                        H3Core h3 = H3Core.newInstance();
                        for (Map<String, Object> mapEdges : polyEdges) {
                            for (Map.Entry<String, Object> entry : mapEdges.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                Double myData = Double.parseDouble(value.toString());

                                if (key.equalsIgnoreCase("lat")){
                                    thisLat = myData;
                                }
                                if (key.equalsIgnoreCase("lon")){
                                    thisLong = myData;
                                }
                            }
                            GeoCoord tmpGeoCoord = new GeoCoord(thisLat, thisLong);
                            hexPoints.add(tmpGeoCoord);                           
                        }

                        for (Map<String, Object> hexHolesMap : polyEdgeHoles) {
                            for (Map.Entry<String, Object> entry : hexHolesMap.entrySet()) {
                                String key = entry.getKey();
                                Object value = entry.getValue();
                                Double myData = Double.parseDouble(value.toString());

                                if (key.equalsIgnoreCase("lat")){
                                    thisLat = myData;
                                }
                                if (key.equalsIgnoreCase("lon")){
                                    thisLong = myData;
                                }
                            }
                            GeoCoord tmpGeoCoord = new GeoCoord(thisLat, thisLong);
                            hexHoles.add(tmpGeoCoord);                           
                        }



                        List<String> hexList = null;
                        if (!hexHoles.isEmpty()){
                            holesList.add(hexHoles);
                            hexList = h3.polyfillAddress(hexPoints,holesList,9);
                        }else {
                            hexList = h3.polyfillAddress(hexPoints,null,9);
                        }

                        Result result = db.execute( "UNWIND $hexs AS hexaddress MATCH (l:Location {hexAddr:hexaddress}) RETURN l;", Collections.singletonMap( "hexs", hexList ) );
                        Iterator<Node> n_column = result.columnAs( "l" );
                        
                        List<Node> reasons;
                        for ( Node nodeIter : Iterators.asIterable( n_column ) )
                        {
                            reasons = new ArrayList<>();
                            reasons.add(nodeIter);
                            map.put(nodeIter,nodeIter.getProperty( "hexAddr" ).toString());
                           results.put(nodeIter,reasons);
                         }
                         
              //          tx1.close();
                        
                    }    catch (Exception e) {
                        System.out.println(e);
                    }  finally {
             //           tx1.close();
                    }
        
                    // return a node and its linked nodes
                    return results.entrySet().stream().map(entry -> {
                        entry.getValue().add(entry.getKey());
                        return new NodeListResult(entry.getValue());
                    });
                } 
                
                @Procedure(name = "com.dfauth.h3.lineBetweenLocations", mode = Mode.WRITE)
                @Description("CALL com.dfauth.h3.lineBetweenLocations(latitude, longitude, latitude,longitude)")
                public Stream<NodeListResult> lineBetweenLocations(@Name("startLat") Double startLat,@Name("startLong") Double startLong, @Name("endLat") Double endLat, @Name("endLong") Double endLong) throws InterruptedException {
                    Map<Node, String> map = new HashMap<Node,String>();
                    Map<Node, List<Node>> results = new HashMap<>();
                
                    Node node = null;
                    Relationship rel = null;
                    String starthexAddr = null;
                    String endhexAddr = null;
    
                try {
                    H3Core h3 = H3Core.newInstance();
                    
                    List<String> hexList = null;
                    starthexAddr = h3.geoToH3Address(startLat, startLong, 9);
                    endhexAddr = h3.geoToH3Address(endLat, endLong, 9);
                    hexList = h3.h3Line(starthexAddr, endhexAddr);
                    Iterator<String> hexListIterator = hexList.iterator();
                    Result result = db.execute( "UNWIND $hexs AS hexaddress MATCH (l:Location {hexAddr:hexaddress}) RETURN l;", Collections.singletonMap( "hexs", hexListIterator ) );
                    Iterator<Node> n_column = result.columnAs( "l" );
                    
                    List<Node> reasons;
                    for ( Node nodeIter : Iterators.asIterable( n_column ) )
                    {
                        reasons = new ArrayList<>();
                        reasons.add(nodeIter);
                        map.put(nodeIter,nodeIter.getProperty( "hexAddr" ).toString());
                       results.put(nodeIter,reasons);
                     }
                     
          //          tx1.close();
                    
                }    catch (Exception e) {
                    System.out.println(e);
                }  finally {
         //           tx1.close();
                }
    
                // return a node and its linked nodes
                return results.entrySet().stream().map(entry -> {
                    entry.getValue().add(entry.getKey());
                    return new NodeListResult(entry.getValue());
                });
            } 
            
}
