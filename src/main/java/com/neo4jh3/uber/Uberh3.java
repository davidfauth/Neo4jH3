package com.neo4jh3.uber;

import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import com.uber.h3core.util.LatLng;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Uberh3 {
    @Context
    public GraphDatabaseService db;

    @Context
    public Transaction tx;

    private final static int DEFAULT_H3_RESOLUTION = 9;

    private static H3Core h3 = null;

    static {
        try {
            h3 = H3Core.newInstance();
        } catch (IOException e) {
            // TODO: switch to logging api
            System.err.println("failed to initialize H3Core");
            e.printStackTrace();
        }
    }

    @UserFunction(name = "com.neo4jh3.h3HexAddress")
    @Description("com.neo4jh3.h3HexAddress(latitude, longitude, resolution) - return the hex address for a given latitude.")
    public String h3HexAddress(
            @Name("latitude") Double latValue,
            @Name("longitude") Double longValue,
            @Name("resolution") Long h3Res) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        if (latValue == null || longValue == null) {
            throw new RuntimeException("invalid lat or long values");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution > 0 && h3Resolution <= 15) {
            return h3.latLngToCellAddress(latValue, longValue, h3Resolution);
        }
        throw new RuntimeException("invalid h3 resolution");
    }

    @UserFunction(name = "com.neo4jh3.h3HexAddressNumber")
    @Description("com.neo4jh3.h3HexAddressNumber(latitude, longitude, resolution) - return the hex address for a given latitude.")
    public Long h3HexAddressNumber(
            @Name("latitude") Double latValue,
            @Name("longitude") Double longValue,
            @Name("resolution") Long h3Res) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        if (latValue == null || longValue == null) {
            throw new RuntimeException("invalid lat or long values");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution > 0 && h3Resolution <= 15) {
            return h3.latLngToCell(latValue, longValue, h3Resolution);
        }
        throw new RuntimeException("invalid h3 resolution");
    }

    @UserFunction(name = "com.neo4jh3.h3RingsForDistance")
    @Description("com.neo4jh3.h3RingsForDistance(resolution, distance) - return the number of rings for a given distance.")
    public Long h3RingsForDistance(
            @Name("h3Res") Long h3Res,
            @Name("distanceValue") Long distanceValue) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        if (distanceValue == null) {
            throw new RuntimeException("invalid distance value");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution > 0 && h3Resolution <= 15) {
            final double hexLength = h3.getHexagonEdgeLengthAvg(h3Resolution, LengthUnit.km);
            final Double numberHexRings = Math.ceil(distanceValue / hexLength);
            return numberHexRings.longValue();
        }
        throw new RuntimeException("invalid h3 resolution");
    }

    @UserFunction(name = "com.neo4jh3.gridDistance")
    @Description("com.neo4jh3.gridDistance(fromHexAddress, toHexAddress) - Provides the distance in grid cells between the two indexes.")
    public double gridDistance(
            @Name("fromHexAddress") String fromHexAddress,
            @Name("toHexAddress") String toHexAddress) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        if (fromHexAddress == null || toHexAddress == null) {
            throw new RuntimeException("invalid from HexAddress or to HexAddress");
        }

        return h3.gridDistance(fromHexAddress, toHexAddress);
        
    }

    @UserFunction(name = "com.neo4jh3.gridDistanceNumber")
    @Description("com.neo4jh3.gridDistanceNumber(fromHexAddress, toHexAddress) - Provides the distance in grid cells between the two indexes.")
    public double gridDistanceNumber(
            @Name("fromHexAddress") Long fromHexAddress,
            @Name("toHexAddress") Long toHexAddress) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        if (fromHexAddress == null || toHexAddress == null) {
            throw new RuntimeException("invalid from HexAddress or to HexAddress");
        }

        return h3.gridDistance(fromHexAddress, toHexAddress);
        
    }

    @UserFunction(name = "com.neo4jh3.cellToParent")
    @Description("CALL com.neo4jh3.cellToParent(hexAddress, h3Resolution)")
    public String cellToParent(@Name("hexAddress") String hexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution > 0 && h3Resolution <= 15) {
            return h3.cellToParentAddress(hexAddress, h3Resolution);
        }
        throw new InterruptedException("invalid h3 resolution");
    }

    @UserFunction(name = "com.neo4jh3.cellToParentNumber")
    @Description("CALL com.neo4jh3.cellToParentNumber(hexAddress, h3Resolution)")
    public Long cellToParentNumber(@Name("hexAddress") Long hexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution > 0 && h3Resolution <= 15) {
            return h3.cellToParent(hexAddress, h3Resolution);
        }
        throw new InterruptedException("invalid h3 resolution");
    }

    @UserFunction(name = "com.neo4jh3.cellToLatLng")
    @Description("CALL com.neo4jh3.cellToLatLng(hexAddress)")
    public String cellToLatLng(@Name("hexAddress") String hexAddress) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        LatLng tmpGeoCoord = h3.cellToLatLng(hexAddress);
        return String.valueOf(tmpGeoCoord.lat) + "," + String.valueOf(tmpGeoCoord.lng);

    }

    @UserFunction(name = "com.neo4jh3.cellToLatLngNumber")
    @Description("CALL com.neo4jh3.cellToLatLngNumber(hexAddress)")
    public String cellToLatLngNumber(@Name("hexAddress") Long hexAddress) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        LatLng tmpGeoCoord = h3.cellToLatLng(hexAddress);
        return String.valueOf(tmpGeoCoord.lat) + "," + String.valueOf(tmpGeoCoord.lng);

    }

    @UserFunction(name = "com.neo4jh3.distanceBetweenHexes")
    @Description("CALL com.neo4jh3.distanceBetweenHexes(fromHexAddress, toHexAddress)")
    public double distanceBetweenHexes(@Name("fromHexAddress") String fromHexAddress, @Name("toHexAddress") String toHexAddress) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (fromHexAddress == null || toHexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        LatLng fromGeoCoord = h3.cellToLatLng(fromHexAddress);
        LatLng toGeoCoord = h3.cellToLatLng(toHexAddress);
        return h3.greatCircleDistance(fromGeoCoord, toGeoCoord, LengthUnit.km);
        
        //double geoDistance = distance(fromGeoCoord.lat, fromGeoCoord.lng, toGeoCoord.lat, toGeoCoord.lng,units);

        //return geoDistance;

    }

    @UserFunction(name = "com.neo4jh3.distanceBetweenHexesNumber")
    @Description("CALL com.neo4jh3.distanceBetweenHexesNumber(fromHexAddress, toHexAddress)")
    public double distanceBetweenHexesNumber(@Name("fromHexAddress") Long fromHexAddress, @Name("toHexAddress") Long toHexAddress) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (fromHexAddress == null || toHexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        LatLng fromGeoCoord = h3.cellToLatLng(fromHexAddress);
        LatLng toGeoCoord = h3.cellToLatLng(toHexAddress);
        return h3.greatCircleDistance(fromGeoCoord, toGeoCoord, LengthUnit.km);
        //double geoDistance = distance(fromGeoCoord.lat, fromGeoCoord.lng, toGeoCoord.lat, toGeoCoord.lng,units);
        //return geoDistance;
    }
/* 
    @Procedure(name = "com.neo4jh3.cellToParentNumber", mode = Mode.READ)
    @Description("CALL com.neo4jh3.cellToParentNumber(hexAddress, h3Resolution)")
    public Stream<H3LongAddress> cellToParentNumber(@Name("hexAddress") Long hexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution > 0 && h3Resolution <= 15) {
            return Stream.of(H3LongAddress.of(h3.cellToParent(hexAddress, h3Resolution)));
        }
        throw new InterruptedException("invalid h3 resolution");
    }
    */
    /* Neo4j H3 Procedures */

    @Procedure(name = "com.neo4jh3.gridDisk", mode = Mode.READ)
    @Description("CALL com.neo4jh3.gridDisk(hexAddress, ringSize)")
    public Stream<H3StringAddress> gridDisk(@Name("hexAddress") String hexAddress, @Name("ringSize") Long ringSize)
            throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null || ringSize == null) {
            throw new InterruptedException("invalid arguments");
        }

        final List<String> ringList = h3.gridDisk(hexAddress, ringSize.intValue());
        return ringList.stream().map(H3StringAddress::of);
    }

    @Procedure(name = "com.neo4jh3.cellToChildren", mode = Mode.READ)
    @Description("CALL com.neo4jh3.cellToChildren(hexAddress, h3Resolution)")
    public Stream<H3StringAddress> cellToChildren(@Name("hexAddress") String hexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution > 0 && h3Resolution <= 15) {
            return h3.cellToChildren(hexAddress, h3Resolution).stream().map(H3StringAddress::of);
        } else {
            throw new InterruptedException("invalid h3Resolution");
        }
    }

    @Procedure(name = "com.neo4jh3.cellToChildrenNumber", mode = Mode.READ)
    @Description("CALL com.neo4jh3.cellToChildrenNumber(hexAddress, h3Resolution)")
    public Stream<H3LongAddress> cellToChildrenNumber(@Name("hexAddress") Long hexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution > 0 && h3Resolution <= 15) {
            return h3.cellToChildren(hexAddress, h3Resolution).stream().map(H3LongAddress::of);
        }
        throw new InterruptedException("invalid h3 resolution");
    }

    /*
     *
     * call com.neo4jh3.polygonToCellsTest(["37.7866,-122.3805","37.7198,-122.3544","37.7076,-122.5123","37.7835,-122.5247","37.8151,-122.4798"],[],7) yield value return value;
     */
    @Procedure(name = "com.neo4jh3.polygonToCells", mode = Mode.READ)
    @Description("CALL com.neo4jh3.polygonToCells(polyEdges, polyEdgeHoles, resolution, latlon order)")
    public Stream<H3StringAddress> polygonToCells(@Name("polyEdges") List<String> polyEdges, @Name("polyEdgeHoles") List<String> polyEdgeHoles, @Name("h3Res") Long h3Res, @Name("latlonorder") String latlonorder) throws InterruptedException {
        if (h3 == null) {
            return Stream.empty();
        }
        if (polyEdges == null || polyEdgeHoles == null) {
            throw new InterruptedException("invalid arguments");
        }

        List<LatLng> hexPoints = new ArrayList<>();
        List<LatLng> hexHoles = new ArrayList<>();
        List<List<LatLng>> holesList = new ArrayList<>();

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution < 1 || h3Resolution > 15) {
            throw new InterruptedException("invalid h3 resolution");
        }

        for (String mapEdges : polyEdges) {
            final String[] latLonList = mapEdges.split(",");
            LatLng tmpGeoCoord = null;
            if (latlonorder.equalsIgnoreCase("latlon")){
                tmpGeoCoord = new LatLng(Double.parseDouble(latLonList[0]), Double.parseDouble(latLonList[1]));
            } else {
                tmpGeoCoord = new LatLng(Double.parseDouble(latLonList[1]), Double.parseDouble(latLonList[0]));
            }
            hexPoints.add(tmpGeoCoord);
        }

        for (String mapEdges : polyEdgeHoles) {
            final String[] latLonList = mapEdges.split(",");
            LatLng tmpGeoCoord = null;
            if (latlonorder.equalsIgnoreCase("latlon")){
                tmpGeoCoord = new LatLng(Double.parseDouble(latLonList[0]), Double.parseDouble(latLonList[1]));
            } else {
                tmpGeoCoord = new LatLng(Double.parseDouble(latLonList[1]), Double.parseDouble(latLonList[2]));
            }
                hexHoles.add(tmpGeoCoord);
        }

        List<String> hexList;
        if (!hexHoles.isEmpty()) {
            holesList.add(hexHoles);
            hexList = h3.polygonToCellAddresses(hexPoints, holesList, h3Resolution);
        } else {
            hexList = h3.polygonToCellAddresses(hexPoints, null, h3Resolution);
        }
        return hexList.stream().map(H3StringAddress::of);
    }

    @Procedure(name = "com.neo4jh3.gridPathCells", mode = Mode.READ)
    @Description("CALL com.neo4jh3.gridPathCells(latitude, longitude, latitude, longitude, h3Resolution)")
    public Stream<H3StringAddress> gridPathCells(@Name("startLat") Double startLat, @Name("startLong") Double startLong, @Name("endLat") Double endLat, @Name("endLong") Double endLong, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (startLat == null || startLong == null || endLat  == null || endLong == null) {
            throw new InterruptedException("invalid arguments");
        }
        if (h3 == null) {
            return Stream.empty();
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution < 1 || h3Resolution > 15) {
            throw new InterruptedException("invalid h3 resolution");
        }

        final String starthexAddr = h3.latLngToCellAddress(startLat, startLong, h3Resolution);
        final String endhexAddr = h3.latLngToCellAddress(endLat, endLong, h3Resolution);
        return h3.gridPathCells(starthexAddr, endhexAddr)
                .stream()
                .map(H3StringAddress::of);
    }

    @Procedure(name = "com.neo4jh3.gridPathCellsHexAddress", mode = Mode.READ)
    @Description("CALL com.neo4jh3.gridPathCellsHexAddress(fromAddress, toAddress, h3Resolution)")
    public Stream<H3StringAddress> gridPathCellsHexAddress(@Name("fromAddress") String fromAddress, @Name("toAddress") String toAddress) throws InterruptedException {
        if (fromAddress == null || toAddress == null) {
            throw new InterruptedException("invalid arguments");
        }
        if (h3 == null) {
            return Stream.empty();
        }

        return h3.gridPathCells(fromAddress, toAddress)
                .stream()
                .map(H3StringAddress::of);
    }

    /*
     * Return list of hex addresses for a line
     */
    /* 
    @Procedure(name = "com.neo4jh3.lineHexAddresses", mode = Mode.READ)
    @Description("CALL com.neo4jh3.lineHexAddresses(polyEdges, h3Resolution)")
    public Stream<H3StringAddress> lineHexAddresses(@Name("polyEdges") List<String> polyEdges, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (polyEdges == null) {
            throw new InterruptedException("invalid polygon edges");
        }
        if (h3 == null) {
            return Stream.empty();
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution < 1 || h3Resolution > 15) {
            throw new InterruptedException("invalid h3 resolution");
        }

        /*  Loop through the list of lat/lon
         *       Convert pair to hexAddress
         *       For hex(0), hex(1)
         *       run gridPathCells and add hexAddress to list
         *   Until you reach last lat/lon
         */
        /*
        if (polyEdges.size() < 1) {
            throw new InterruptedException("invalid amount of polygon edges");
        }

        final List<String> addresses = polyEdges.stream()
                .map(s -> s.split(","))
                .map(tuple -> new double[]{Double.parseDouble(tuple[0]), Double.parseDouble(tuple[1])})
                .map(tuple -> h3.latLngToCellAddress(tuple[0], tuple[1], h3Resolution))
                .toList();

        final Iterator<List<String>> iterator = new Iterator<>() {
            private int index = 0;
            private String prevAddress = null;

            @Override
            public boolean hasNext() {
                return index < addresses.size();
            }

            @Override
            public List<String> next() {
                if (prevAddress == null) {
                    // Must be first tic...
                    prevAddress = addresses.get(index);
                    index++;
                }

                final String nextAddress = addresses.get(index);
                final List<String> list = h3.gridPathCells(prevAddress, nextAddress);
                prevAddress = nextAddress;

                return list;
            }
        };

        // XXX Not sure if this is correct...
        final Spliterator<List<String>> spliterator = Spliterators.spliterator(iterator, addresses.size() - 1, Spliterator.ORDERED);

        return StreamSupport
                .stream(spliterator, false)
                .flatMap(Collection::stream)
                .map(H3StringAddress::of);
    }
 */
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		if ((lat1 == lat2) && (lon1 == lon2)) {
			return 0;
		}
		else {
			double theta = lon1 - lon2;
			double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;
			if (unit.equals("K")) {
				dist = dist * 1.609344;
			} else if (unit.equals("N")) {
				dist = dist * 0.8684;
			}
			return (dist);
		}
	}

}
