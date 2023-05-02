package com.neo4jh3.uber;

import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import com.uber.h3core.util.LatLng;

import mil.nga.sf.Geometry;
import mil.nga.sf.Point;
import mil.nga.sf.geojson.FeatureConverter;
import mil.nga.sf.util.ByteWriter;
import mil.nga.sf.util.TextReader;
import mil.nga.sf.wkb.GeometryWriter;
import mil.nga.sf.wkt.GeometryReader;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.procedure.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
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

    @UserFunction(name = "com.neo4jh3.h3Validate")
    @Description("com.neo4jh3.h3Validate(hexAddress) - validate a long hex Address.")
    public Long h3Validate(
            @Name("hexAddress") Long hexAddress) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        
        try {
            if (h3.isValidCell(hexAddress)){
                return hexAddress;
            } else {
                // throw new RuntimeException("invalid h3 resolution");
                return -1L;
            }
        } catch (NumberFormatException nfe) {
            // throw new RuntimeException("invalid h3 resolution");
            return -1L;
        }
        
    }

    @UserFunction(name = "com.neo4jh3.h3ValidateString")
    @Description("com.neo4jh3.h3ValidateString(hexAddress) - validate a string hex Address.")
    public String h3ValidateString(
            @Name("hexAddress") String hexAddress) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        try {
            if (h3.isValidCell(hexAddress)){
                return hexAddress;
            } else {
                // throw new RuntimeException("invalid h3 resolution");
                return "-1";
            }
        } catch (NumberFormatException nfe) {
            // throw new RuntimeException("invalid h3 resolution");
            return "-1";
        }
    }

    @UserFunction(name = "com.neo4jh3.h3HexAddress")
    @Description("com.neo4jh3.h3HexAddress(latitude, longitude, resolution) - return the hex address for a given latitude.")
    public Long h3HexAddress(
            @Name("latitude") Double latValue,
            @Name("longitude") Double longValue,
            @Name("resolution") Long h3Res) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        Long returnValue = 0L;
        int validLatLon = 1;
        if (latValue == null || longValue == null) {
            returnValue = -1L;
        }

        if ( latValue>90 || latValue<-90 ){
            validLatLon = 0;
            returnValue = -3L;
        }

        if ( longValue>180 || longValue<-180 ){
            validLatLon = 0;
            returnValue = -4L;
        }


        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (validLatLon > 0){
            if (h3Resolution > 0 && h3Resolution <= 15) {
                returnValue = h3.latLngToCell(latValue, longValue, h3Resolution);
            } else {
                returnValue = -2L;
            }
        }
        return returnValue;
    }

    @UserFunction(name = "com.neo4jh3.h3HexAddressString")
    @Description("com.neo4jh3.h3HexAddressString(latitude, longitude, resolution) - return the hex address for a given latitude.")
    public String h3HexAddressString(
            @Name("latitude") Double latValue,
            @Name("longitude") Double longValue,
            @Name("resolution") Long h3Res) {
        String returnString = "";
        int validLatLon = 1;
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        if (latValue == null || longValue == null) {
            returnString = "NULL";
        }

        if ( latValue>90 || latValue<-90 ){
            validLatLon = 0;
            returnString = "-3";
        }

        if ( longValue>180 || longValue<-180 ){
            validLatLon = 0;
            returnString = "-4";
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (validLatLon > 0){
            if (h3Resolution > 0 && h3Resolution <= 15) {
                returnString = h3.latLngToCellAddress(latValue, longValue, h3Resolution);
            } else {
                returnString = "-2";
            }
        }
        return returnString;
    }

    

    @UserFunction(name = "com.neo4jh3.h3tostring")
    @Description("com.neo4jh3.h3tostring(longHex) - return the string value of a long hex address.")
    public String h3tostringFunction(
            @Name("longHex") Long longHex) {
        String returnString = "";
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        
        try {
            if (h3.isValidCell(longHex)){
                returnString = h3.h3ToString(longHex);
            } else {
                returnString = "-1";
            }
            return returnString;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            returnString = "-1";
            return returnString;
        }
    }

    @UserFunction(name = "com.neo4jh3.stringToH3")
    @Description("com.neo4jh3.stringToH3(longHex) - return the string value of a long hex address.")
    public Long stringToH3Function(
            @Name("strHexAddress") String strHexAddress) {
        Long returnString = 0L;
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        
        try {
            if (h3.isValidCell(strHexAddress)){
                returnString = h3.stringToH3(strHexAddress);
            } else {
                returnString = -1L;
            }
            return returnString;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            returnString = -1L;
            return returnString;
        }
    }

    @UserFunction(name = "com.neo4jh3.h3ResolutionString")
    @Description("com.neo4jh3.h3ResolutionString(longHex) - return the resolution of a string hex address.")
    public Long h3ResolutionString(
            @Name("strHexAddress") String strHexAddress) {
        Long returnString = 0L;
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        
        try {
            if (h3.isValidCell(strHexAddress)){
                returnString = Long.valueOf(h3.getResolution(strHexAddress));
            } else {
                returnString = -1L;
            }
            return returnString;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            returnString = -1L;
            return returnString;
        }
    }

    @UserFunction(name = "com.neo4jh3.h3Resolution")
    @Description("com.neo4jh3.h3Resolution(longHex) - return the string value of a long hex address.")
    public Long h3Resolution(
            @Name("hexAddress") Long hexAddress) {
                Long returnString = 0L;
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }

        try {
            if (h3.isValidCell(hexAddress)){
                returnString = Long.valueOf(h3.getResolution(hexAddress));
            } else {
                returnString = -1L;
            }
            return returnString;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            returnString = -1L;
            return returnString;
        }
    }


    @UserFunction(name = "com.neo4jh3.latlongash3String")
    @Description("com.neo4jh3.latlongash3String(latitude, longitude, resolution) - return the hex address for a given latitude.")
    public String latlongash3String(
            @Name("latitude") Double latValue,
            @Name("longitude") Double longValue,
            @Name("resolution") Long h3Res) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        if (latValue == null || longValue == null) {
            throw new RuntimeException("invalid lat or long values");
        }

        String returnString = "";
        int validLatLon = 1;
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        if (latValue == null || longValue == null) {
            returnString = "-5";
        }

        if ( latValue>90 || latValue<-90 ){
            validLatLon = 0;
            returnString = "-3";
        }

        if ( longValue>180 || longValue<-180 ){
            validLatLon = 0;
            returnString = "-4";
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (validLatLon > 0){
            if (h3Resolution > 0 && h3Resolution <= 15) {
                returnString = h3.latLngToCellAddress(latValue, longValue, h3Resolution);
            } else {
                returnString = "-2";
            }
        }
        return returnString;
    }

    @UserFunction(name = "com.neo4jh3.latlongash3")
    @Description("com.neo4jh3.latlongash3(latitude, longitude, resolution) - return the hex address for a given latitude.")
    public Long latlongash3(
            @Name("latitude") Double latValue,
            @Name("longitude") Double longValue,
            @Name("resolution") Long h3Res) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        
        Long returnValue = 0L;
        int validLatLon = 1;
        if (latValue == null || longValue == null) {
            returnValue = -1L;
        }

        if ( latValue>90 || latValue<-90 ){
            validLatLon = 0;
            returnValue = -3L;
        }

        if ( longValue>180 || longValue<-180 ){
            validLatLon = 0;
            returnValue = -4L;
        }


        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (validLatLon > 0){
            if (h3Resolution > 0 && h3Resolution <= 15) {
                returnValue = h3.latLngToCell(latValue, longValue, h3Resolution);
            } else {
                returnValue = -2L;
            }
        }
        return returnValue;
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
    public Long gridDistance(
            @Name("fromHexAddress") Long fromHexAddress,
            @Name("toHexAddress") Long toHexAddress) {

        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        Long returnValue = 0L;
        if (fromHexAddress == null || toHexAddress == null) {
            throw new RuntimeException("invalid from HexAddress or to HexAddress");
        }
        if (h3.isValidCell(fromHexAddress) && h3.isValidCell(toHexAddress)){
            returnValue = h3.gridDistance(fromHexAddress, toHexAddress);
        } else {
            if (!h3.isValidCell(fromHexAddress) || !h3.isValidCell(toHexAddress)){
                returnValue = -1L;
            }
        }


        return returnValue;
        
        
    }

    @UserFunction(name = "com.neo4jh3.gridDistanceString")
    @Description("com.neo4jh3.gridDistanceString(fromHexAddress, toHexAddress) - Provides the distance in grid cells between the two indexes.")
    public double gridDistanceString(
            @Name("fromHexAddress") String fromHexAddress,
            @Name("toHexAddress") String toHexAddress) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        if (fromHexAddress == null || toHexAddress == null) {
            throw new RuntimeException("invalid from HexAddress or to HexAddress");
        }

        Long returnValue = 0L;
        if (fromHexAddress == null || toHexAddress == null) {
            throw new RuntimeException("invalid from HexAddress or to HexAddress");
        }
        if (h3.isValidCell(fromHexAddress) && h3.isValidCell(toHexAddress)){
            returnValue = h3.gridDistance(fromHexAddress, toHexAddress);
        } else {
            if (!h3.isValidCell(fromHexAddress) || !h3.isValidCell(toHexAddress)){
                returnValue = -1L;
            }
        }
        return returnValue;
        
    }

    @UserFunction(name = "com.neo4jh3.toparent")
    @Description("CALL com.neo4jh3.toparent(hexAddress, h3Resolution)")
    public Long toparent(@Name("hexAddress") Long hexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        Long returnValue = 0L;
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        int h3MaxRes = h3.getResolution(hexAddress);
        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        
        if (h3Resolution > 0 && h3Resolution <= h3MaxRes && h3.isValidCell(hexAddress)) {
            returnValue = h3.cellToParent(hexAddress, h3Resolution);
        } else {
            if (h3Resolution < 0 || h3Resolution > h3MaxRes){
                returnValue = -2L;
            }
            if (!h3.isValidCell(hexAddress)){
                returnValue = -1L;
            }
        }
        return returnValue;
    }

    @UserFunction(name = "com.neo4jh3.toparentString")
    @Description("CALL com.neo4jh3.toparentString(hexAddress, h3Resolution)")
    public String toparentString(@Name("hexAddress") String hexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        String returnValue = "";
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        int h3MaxRes = h3.getResolution(hexAddress);
        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution > 0 && h3Resolution <= h3MaxRes && h3.isValidCell(hexAddress)) {
            returnValue = h3.cellToParentAddress(hexAddress, h3Resolution);
        } else {
            if (h3Resolution < 0 || h3Resolution > h3MaxRes){
                returnValue = "-2";
            }
            if (!h3.isValidCell(hexAddress)){
                returnValue = "-1";
            }
        }
        return returnValue;
    }

    @UserFunction(name = "com.neo4jh3.cellToLatLng")
    @Description("CALL com.neo4jh3.cellToLatLng(hexAddress)")
    public String cellToLatLng(@Name("hexAddress") Long hexAddress) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }
        if (h3.isValidCell(hexAddress)){
            LatLng tmpGeoCoord = h3.cellToLatLng(hexAddress);
            return String.valueOf(tmpGeoCoord.lat) + "," + String.valueOf(tmpGeoCoord.lng);
        } else { 
            return "-1";
        }

    }
    @UserFunction(name = "com.neo4jh3.cellToLatLngString")
    @Description("CALL com.neo4jh3.cellToLatLngString(hexAddress)")
    public String cellToLatLngString(@Name("hexAddress") String hexAddress) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        if (h3.isValidCell(hexAddress)){
            LatLng tmpGeoCoord = h3.cellToLatLng(hexAddress);
            return String.valueOf(tmpGeoCoord.lat) + "," + String.valueOf(tmpGeoCoord.lng);
        } else {
            return "-1";
        }

    }

    @UserFunction(name = "com.neo4jh3.distanceBetweenHexes")
    @Description("CALL com.neo4jh3.distanceBetweenHexes(fromHexAddress, toHexAddress)")
    public double distanceBetweenHexes(@Name("fromHexAddress") Long fromHexAddress, @Name("toHexAddress") Long toHexAddress) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (fromHexAddress == null || toHexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }
        if (h3.isValidCell(fromHexAddress) && h3.isValidCell(toHexAddress)){
            LatLng fromGeoCoord = h3.cellToLatLng(fromHexAddress);
            LatLng toGeoCoord = h3.cellToLatLng(toHexAddress);
            return h3.greatCircleDistance(fromGeoCoord, toGeoCoord, LengthUnit.km);
        } else {
            return -1.0;
        }
        //double geoDistance = distance(fromGeoCoord.lat, fromGeoCoord.lng, toGeoCoord.lat, toGeoCoord.lng,units);
        //return geoDistance;
    }

    @UserFunction(name = "com.neo4jh3.distanceBetweenHexesString")
    @Description("CALL com.neo4jh3.distanceBetweenHexesString(fromHexAddress, toHexAddress)")
    public double distanceBetweenHexesString(@Name("fromHexAddress") String fromHexAddress, @Name("toHexAddress") String toHexAddress) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (fromHexAddress == null || toHexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }
        if (h3.isValidCell(fromHexAddress) && h3.isValidCell(fromHexAddress)){
            LatLng fromGeoCoord = h3.cellToLatLng(fromHexAddress);
            LatLng toGeoCoord = h3.cellToLatLng(toHexAddress);
            return h3.greatCircleDistance(fromGeoCoord, toGeoCoord, LengthUnit.km);
        } else {
            return -1.0;
        }

    }

    @UserFunction(name = "com.neo4jh3.minChild")
    @Description("CALL com.neo4jh3.minChild(hexAddress, h3Resolution)")
    public Long minChild(@Name("fromHexAddress") Long fromHexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (fromHexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        int h3CurRes = h3.getResolution(fromHexAddress);
        
        if (h3.isValidCell(fromHexAddress)){
            if (h3Resolution > h3CurRes && h3Resolution <= 15) {
                Long minChild = 648302213769948598L;
                Long curChild = 0L;
                List<Long> listChildren = h3.cellToChildren(fromHexAddress, h3Resolution);
                ListIterator<Long>
                    iterator = listChildren.listIterator();
                    while (iterator.hasNext()) {
                        curChild = iterator.next();
                        if (curChild < minChild){
                            minChild = curChild;
                        }
                    }
                return minChild;
            } else {
                return -2L;
            }
        } else {
            return -1L;
        }
    }

    @UserFunction(name = "com.neo4jh3.minChildString")
    @Description("CALL com.neo4jh3.minChildString(hexAddress, h3Resolution)")
    public String minChildString(@Name("fromHexAddress") String fromHexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (fromHexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }
        int h3CurRes = h3.getResolution(fromHexAddress);
        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3.isValidCell(fromHexAddress)){
            if (h3Resolution > h3CurRes && h3Resolution <= 15) {
                String minChild = "8ff3b6db6db6db6";
                String curChild = "";
                List<String> listChildren = h3.cellToChildren(fromHexAddress, h3Resolution);
                ListIterator<String>
                iterator = listChildren.listIterator();
                while (iterator.hasNext()) {
                    curChild = iterator.next();
                    if (curChild.compareTo(minChild) < 0){
                        minChild = curChild;
                    }
                }
                return minChild;
            } else {
                return "-2";
            }
        } else {
            return "-1";
        }
    }

    @UserFunction(name = "com.neo4jh3.maxChild")
    @Description("CALL com.neo4jh3.maxChild(hexAddress, h3Resolution)")
    public Long maxChild(@Name("fromHexAddress") Long fromHexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (fromHexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        int h3CurRes = h3.getResolution(fromHexAddress);
        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3.isValidCell(fromHexAddress)){
            if (h3Resolution > h3CurRes && h3Resolution <= 15) {
                Long maxChild = 0L;
                Long curChild = 0L;
                List<Long> listChildren = h3.cellToChildren(fromHexAddress, h3Resolution);
                ListIterator<Long>
                iterator = listChildren.listIterator();
                while (iterator.hasNext()) {
                    curChild = iterator.next();
                    //System.out.println(curChild);
                    if (curChild > maxChild){
                        maxChild = curChild;
                    }
                }
                return maxChild;
            } else {
                return -2L;
            }
        } else {
            return -1L;
        }
    }

    @UserFunction(name = "com.neo4jh3.maxChildString")
    @Description("CALL com.neo4jh3.maxChildString(hexAddress, h3Resolution)")
    public String maxChild(@Name("fromHexAddress") String fromHexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (fromHexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        int h3CurRes = h3.getResolution(fromHexAddress);
        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3.isValidCell(fromHexAddress)){
            if (h3Resolution > h3CurRes && h3Resolution <= 15) {
                String maxChild = "8001fffffffffff";
                String curChild = "";
                List<String> listChildren = h3.cellToChildren(fromHexAddress, h3Resolution);
                ListIterator<String>
                iterator = listChildren.listIterator();
                while (iterator.hasNext()) {
                    curChild = iterator.next();
                    //System.out.println(curChild);
                    if (curChild.compareTo(maxChild) > 0){
                        maxChild = curChild;
                    }
                }
                return maxChild;
            } else {
                return "-2";
            }
        } else {
            return "-1";
        }
    }

    // Geography Functions
    @UserFunction(name = "com.neo4jh3.pointash3")
    @Description("com.neo4jh3.pointash3(wktString, resolution) - Provides the distance in grid cells between the two indexes.")
    public Long pointash3(
            @Name("wktString") String wktString, 
            @Name("h3Res") Long h3Res) throws InterruptedException 
            {
            Long h3Address = 0L;
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
    
            final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();

            try {
                if (h3Resolution > 0 && h3Resolution <= 15) { 
                    Geometry geometry = GeometryReader.readGeometry(wktString);
                   if (geometry.getGeometryType().toString().equalsIgnoreCase("Point")){
                        h3Address=h3.latLngToCell(geometry.getEnvelope().getMinX(), geometry.getEnvelope().getMinY(), h3Resolution);
                    }
                } else {
                    h3Address = -2L;
                }
            } catch (Exception e) {
                //System.out.println(e);
                h3Address = -1L;
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        return h3Address;
    }
    @UserFunction(name = "com.neo4jh3.pointash3String")
    @Description("com.neo4jh3.pointash3String(wktString, resolution) - Provides the distance in grid cells between the two indexes.")
    public String pointash3String(
            @Name("wktString") String wktString, 
            @Name("h3Res") Long h3Res) throws InterruptedException 
            {
            String h3Address = "";
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
    
            final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();

            try {
                if (h3Resolution > 0 && h3Resolution <= 15) { 
                    Geometry geometry = GeometryReader.readGeometry(wktString);
                   if (geometry.getGeometryType().toString().equalsIgnoreCase("Point")){
                        h3Address=h3.latLngToCellAddress(geometry.getEnvelope().getMinX(), geometry.getEnvelope().getMinY(), h3Resolution);
                    }
                } else {
                    h3Address = "-2";
                }
            } catch (Exception e) {
                //System.out.println(e);
                h3Address = "-1";
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        return h3Address;
    }

    // New Geo Functions
    @Procedure(name = "com.neo4jh3.multilineash3", mode = Mode.READ)
    @Description("com.neo4jh3.multilineash3(wktString, resolution) - Provides the distance in grid cells between the two indexes.")
        public Stream<H3LongAddress> multilineash3(
            @Name("wktString") String wktString, 
            @Name("h3Res") Long h3Res) throws InterruptedException 
            {
            List<Long> listh3Address = new ArrayList<Long>();
            List<Long> gpCells = new ArrayList<Long>();
            Long h3Address = 0L;
            Long h3StartAddress = 0L;
            Long h3MidAddress = 0L;
            Long h3EndAddress = 0L;
            Double fromLat = 0.0;
			Double fromLon = 0.0;
			Double toLat = 0.0;
			Double toLon = 0.0;
			Double midLat = 0.0;
			Double midLon = 0.0;
            String mls = "";

            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
    
            final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();

            try {
                if (h3Resolution > 0 && h3Resolution <= 15) { 
                    mls = wktString.replace("MULTILINESTRING((", "");
			        mls = mls.replace(" (","");
			        mls = mls.replace(")","");
                    mls = mls.replace("(","");
			        String[] latlonPairs = mls.split(",");
			        for (int i = 0; i < latlonPairs.length; i++) {
				        if (i > 0){
					        fromLat = Double.valueOf(latlonPairs[i-1].split(" ")[0]);
					        fromLon = Double.valueOf(latlonPairs[i-1].split(" ")[1]);
					        toLat = Double.valueOf(latlonPairs[i].split(" ")[0]);
					        toLon = Double.valueOf(latlonPairs[i].split(" ")[1]);
					        midLat = (fromLat + toLat) / 2;
					        midLon = (fromLon + toLon) / 2;
                            h3StartAddress = h3.latLngToCell(fromLat, fromLon, h3Resolution);
                            h3MidAddress = h3.latLngToCell(midLat, midLon, h3Resolution);
                            h3EndAddress = h3.latLngToCell(toLat, toLon, h3Resolution);
                            try {
                                gpCells = h3.gridPathCells(h3StartAddress, h3MidAddress);
                                for (int j = 0; j < gpCells.size(); j++) {
                                    listh3Address.add(gpCells.get(j));
                                }
                                gpCells.clear();  
                            } catch (Exception e1){                
                            }
                            try {
                                gpCells = h3.gridPathCells((h3MidAddress), h3EndAddress);
                                for (int j = 0; j < gpCells.size(); j++) {
                                    listh3Address.add(gpCells.get(j));
                                }
                                gpCells.clear();  
                            } catch (Exception e1){

                            }

				        }
			        }
                } else {
                    listh3Address = Collections.singletonList(-2L);
                
                }
            } catch (Exception e) {
                //System.out.println(e);
                listh3Address = Collections.singletonList(-1L);
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
            return listh3Address.stream().map(H3LongAddress::of);
    }

       // New Geo Functions
       @Procedure(name = "com.neo4jh3.multilineash3String", mode = Mode.READ)
       @Description("com.neo4jh3.multilineash3String(wktString, resolution) - Provides the distance in grid cells between the two indexes.")
           public Stream<H3StringAddress> multilineash3String(
               @Name("wktString") String wktString, 
               @Name("h3Res") Long h3Res) throws InterruptedException 
               {
               List<String> listh3Address = new ArrayList<String>();
               List<String> gpCells = new ArrayList<String>();
               String h3StartAddress = "";
               String h3MidAddress = "";
               String h3EndAddress = "";
               Double fromLat = 0.0;
               Double fromLon = 0.0;
               Double toLat = 0.0;
               Double toLon = 0.0;
               Double midLat = 0.0;
               Double midLon = 0.0;
               String mls = "";
   
               if (h3 == null) {
                   throw new InterruptedException("h3 failed to initialize");
               }
       
               final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
   
               try {
                   if (h3Resolution > 0 && h3Resolution <= 15) { 
                       mls = wktString.replace("MULTILINESTRING((", "");
                       mls = mls.replace(" (","");
                       mls = mls.replace(")","");
                       mls = mls.replace("(","");
                       String[] latlonPairs = mls.split(",");
                       for (int i = 0; i < latlonPairs.length; i++) {
                           if (i > 0){
                               fromLat = Double.valueOf(latlonPairs[i-1].split(" ")[0]);
                               fromLon = Double.valueOf(latlonPairs[i-1].split(" ")[1]);
                               toLat = Double.valueOf(latlonPairs[i].split(" ")[0]);
                               toLon = Double.valueOf(latlonPairs[i].split(" ")[1]);
                               midLat = (fromLat + toLat) / 2;
                               midLon = (fromLon + toLon) / 2;
                               h3StartAddress = h3.latLngToCellAddress(fromLat, fromLon, h3Resolution);
                               h3MidAddress = h3.latLngToCellAddress(midLat, midLon, h3Resolution);
                               h3EndAddress = h3.latLngToCellAddress(toLat, toLon, h3Resolution);
                               try {
                                   gpCells = h3.gridPathCells(h3StartAddress, h3MidAddress);
                                   for (int j = 0; j < gpCells.size(); j++) {
                                       listh3Address.add(gpCells.get(j));
                                   }
                                   gpCells.clear();  
                               } catch (Exception e1){
                                    //listh3Address = Collections.singletonList("-1");
                               }
                               try {
                                   gpCells = h3.gridPathCells((h3MidAddress), h3EndAddress);
                                   for (int j = 0; j < gpCells.size(); j++) {
                                       listh3Address.add(gpCells.get(j));
                                   }
                                   gpCells.clear();  
                               } catch (Exception e1){
                                    //listh3Address = Collections.singletonList("-1");
                               }
                           }
                       }
                   } else {
                        listh3Address = Collections.singletonList("-2");
                   }
               } catch (Exception e) {
                   //System.out.println(e);
                   listh3Address = Collections.singletonList("-1");
                   // TODO Auto-generated catch block
                   //e.printStackTrace();
               }
               return listh3Address.stream().map(H3StringAddress::of);
       }
    // Geography Functions
    
    @UserFunction(name = "com.neo4jh3.centeraswkb")
    @Description("com.neo4jh3.centeraswkb(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String centeraswkb(
            @Name("hexAddress") Long hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null;
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
    
            try {
                if (h3.isValidCell(hexAddress)){
                    centerPoint = h3.cellToLatLng(hexAddress);
                    Double lat = centerPoint.lat;
                    Double lng = centerPoint.lng;
                    Point point = new Point(false, false, lat, lng);
                    byte[] bytes = GeometryWriter.writeGeometry(point);
                    String hex = "";
                    for (byte i : bytes) {
                        geoJsonString += String.format("%02X", i);
                    }

                } else {
                    geoJsonString = "-1";
                }
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                geoJsonString = "Error getting value";
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.centeraswkbString")
    @Description("com.neo4jh3.centeraswkbString(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String centeraswkbString(
            @Name("hexAddress") String hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null;
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
    
            try {
                if (h3.isValidCell(hexAddress)){
                    centerPoint = h3.cellToLatLng(hexAddress);
                    Double lat = centerPoint.lat;
                    Double lng = centerPoint.lng;
                    Point point = new Point(false, false, lat, lng);
                    byte[] bytes = GeometryWriter.writeGeometry(point);
                    String hex = "";
                    for (byte i : bytes) {
                        geoJsonString += String.format("%02X", i);
                    }

                } else {
                    geoJsonString = "-1";
                }
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                geoJsonString = "Error getting value";
            }
        return geoJsonString;
    }
    @UserFunction(name = "com.neo4jh3.centeraswkt")
    @Description("com.neo4jh3.centeraswkt(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String centeraswkt(
            @Name("hexAddress") Long hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null;
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
    
            try {
                if (h3.isValidCell(hexAddress)){
                    centerPoint = h3.cellToLatLng(hexAddress);
                    Double lat = centerPoint.lat;
                    Double lng = centerPoint.lng;
                    Point point = new Point(false, false, lat, lng);
                    geoJsonString = mil.nga.sf.wkt.GeometryWriter.writeGeometry(point);

                } else {
                    geoJsonString = "-1";
                }
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                geoJsonString = "Error getting value";
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.centeraswktString")
    @Description("com.neo4jh3.centeraswktString(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String centeraswktString(
            @Name("hexAddress") String hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null;
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
    
            try {
                if (h3.isValidCell(hexAddress)){
                    centerPoint = h3.cellToLatLng(hexAddress);
                    Double lat = centerPoint.lat;
                    Double lng = centerPoint.lng;
                    Point point = new Point(false, false, lat, lng);
                    geoJsonString = mil.nga.sf.wkt.GeometryWriter.writeGeometry(point);

                } else {
                    geoJsonString = "-1";
                }
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                geoJsonString = "Error getting value";
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.boundaryaswkt")
    @Description("com.neo4jh3.boundaryaswkt(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String boundaryaswkt(
            @Name("hexAddress") Long hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null; 
            List<LatLng> hexPoints = new ArrayList<>();
            
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
            
            try {
                if (h3.isValidCell(hexAddress)){
                    hexPoints = h3.cellToBoundary(hexAddress);
                    // Iterating ArrayList using Iterator
                    Iterator<LatLng> it = hexPoints.iterator();
                    int i = 0;
                    mil.nga.sf.LinearRing ring = new mil.nga.sf.LinearRing(false, false);
                    Point tpfirst = new Point();
                    while (it.hasNext()){
                        LatLng thisHex = it.next();

                        if (i < 1){
                            tpfirst =  new Point(false, false, thisHex.lat, thisHex.lng);
                            //ring.addPoint(tpfirst);
                        }
                            Point tp =  new Point(false, false, thisHex.lat, thisHex.lng);
                            ring.addPoint(tp);
                        i++;
                    } 
                    ring.addPoint(tpfirst);
                    mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(ring);
                    geoJsonString = mil.nga.sf.wkt.GeometryWriter.writeGeometry(polygon);
                } else {
                    geoJsonString = "-1";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.boundaryaswktString")
    @Description("com.neo4jh3.boundaryaswktString(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String boundaryaswktString(
            @Name("hexAddress") String hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null; 
            List<LatLng> hexPoints = new ArrayList<>();
            
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
            
            try {
                if (h3.isValidCell(hexAddress)){
                    hexPoints = h3.cellToBoundary(hexAddress);
                    // Iterating ArrayList using Iterator
                    Iterator<LatLng> it = hexPoints.iterator();
                    int i = 0;
                    mil.nga.sf.LinearRing ring = new mil.nga.sf.LinearRing(false, false);
                    Point tpfirst = new Point();
                    while (it.hasNext()){
                        LatLng thisHex = it.next();

                        if (i < 1){
                            tpfirst =  new Point(false, false, thisHex.lat, thisHex.lng);
                            //ring.addPoint(tpfirst);
                        }
                            Point tp =  new Point(false, false, thisHex.lat, thisHex.lng);
                            ring.addPoint(tp);
                        i++;
                    } 
                    ring.addPoint(tpfirst);
                    mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(ring);
                    geoJsonString = mil.nga.sf.wkt.GeometryWriter.writeGeometry(polygon);
                } else {
                    geoJsonString = "-1";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.boundaryaswkb")
    @Description("com.neo4jh3.boundaryaswkb(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String boundaryaswkb(
            @Name("hexAddress") Long hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null; 
            List<LatLng> hexPoints = new ArrayList<>();
            
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
            
            try {
                if (h3.isValidCell(hexAddress)){
                    hexPoints = h3.cellToBoundary(hexAddress);
                    // Iterating ArrayList using Iterator
                    Iterator<LatLng> it = hexPoints.iterator();
                    int i = 0;
                    mil.nga.sf.LinearRing ring = new mil.nga.sf.LinearRing(false, false);
                    Point tpfirst = new Point();
                    while (it.hasNext()){
                        LatLng thisHex = it.next();

                        if (i < 1){
                            tpfirst =  new Point(false, false, thisHex.lat, thisHex.lng);
                            //ring.addPoint(tpfirst);
                        }
                            Point tp =  new Point(false, false, thisHex.lat, thisHex.lng);
                            ring.addPoint(tp);
                        i++;
                    } 
                    ring.addPoint(tpfirst);
                    mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(ring);
                    byte[] bytes = GeometryWriter.writeGeometry(polygon);
                    String hex = "";
                    for (byte bi : bytes) {
                        geoJsonString += String.format("%02X", bi);
                    }

                } else {
                    geoJsonString = "-1";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.boundaryaswkbString")
    @Description("com.neo4jh3.boundaryaswkbString(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String boundaryaswkbString(
            @Name("hexAddress") String hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null; 
            List<LatLng> hexPoints = new ArrayList<>();
            
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
            
            try {
                if (h3.isValidCell(hexAddress)){
                    hexPoints = h3.cellToBoundary(hexAddress);
                    // Iterating ArrayList using Iterator
                    Iterator<LatLng> it = hexPoints.iterator();
                    int i = 0;
                    mil.nga.sf.LinearRing ring = new mil.nga.sf.LinearRing(false, false);
                    Point tpfirst = new Point();
                    while (it.hasNext()){
                        LatLng thisHex = it.next();

                        if (i < 1){
                            tpfirst =  new Point(false, false, thisHex.lat, thisHex.lng);
                            //ring.addPoint(tpfirst);
                        }
                            Point tp =  new Point(false, false, thisHex.lat, thisHex.lng);
                            ring.addPoint(tp);
                        i++;
                    } 
                    ring.addPoint(tpfirst);
                    mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(ring);
                    byte[] bytes = GeometryWriter.writeGeometry(polygon);
                    String hex = "";
                    for (byte bi : bytes) {
                        geoJsonString += String.format("%02X", bi);
                    }

                } else {
                    geoJsonString = "-1";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.centerasgeojson")
    @Description("com.neo4jh3.centerasgeojson(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String centerasgeojson(
            @Name("hexAddress") Long hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null;
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
    
            try {
                if (h3.isValidCell(hexAddress)){
                    centerPoint = h3.cellToLatLng(hexAddress);
                    Double lat = centerPoint.lat;
                    Double lng = centerPoint.lng;
                    Point point = new Point(false, false, lat, lng);
                    geoJsonString = FeatureConverter.toStringValue(point);

                } else {
                    geoJsonString = "-1";
                }
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                geoJsonString = "Error getting value";
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.centerasgeojsonString")
    @Description("com.neo4jh3.centerasgeojsonString(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String centerasgeojsonString(
            @Name("hexAddress") String hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null;
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
    
            try {
                if (h3.isValidCell(hexAddress)){
                    centerPoint = h3.cellToLatLng(hexAddress);
                    Double lat = centerPoint.lat;
                    Double lng = centerPoint.lng;
                    Point point = new Point(false, false, lat, lng);
                    geoJsonString = FeatureConverter.toStringValue(point);
                } else {
                    geoJsonString = "-1";
                }
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                geoJsonString = "Error getting value";
            }
        return geoJsonString;
    }


    @UserFunction(name = "com.neo4jh3.boundaryasgeojson")
    @Description("com.neo4jh3.boundaryasgeojson(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String boundaryasgeojson(
            @Name("hexAddress") Long hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null; 
            List<LatLng> hexPoints = new ArrayList<>();
            
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
            
            try {
                if (h3.isValidCell(hexAddress)){
                    hexPoints = h3.cellToBoundary(hexAddress);
                    // Iterating ArrayList using Iterator
                    Iterator<LatLng> it = hexPoints.iterator();
                    int i = 0;
                    mil.nga.sf.LinearRing ring = new mil.nga.sf.LinearRing(false, false);
                    Point tpfirst = new Point();
                    while (it.hasNext()){
                        LatLng thisHex = it.next();

                        if (i < 1){
                            tpfirst =  new Point(false, false, thisHex.lat, thisHex.lng);
                            //ring.addPoint(tpfirst);
                        }
                            Point tp =  new Point(false, false, thisHex.lat, thisHex.lng);
                            ring.addPoint(tp);
                        i++;
                    } 
                    ring.addPoint(tpfirst);
                    mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(ring);
                    geoJsonString = FeatureConverter.toStringValue(polygon);
                } else {
                    geoJsonString = "-1";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.boundaryasgeojsonString")
    @Description("com.neo4jh3.boundaryasgeojsonString(hexAddress) - Provides the distance in grid cells between the two indexes.")
    public String boundaryasgeojsonString(
            @Name("hexAddress") String hexAddress) throws InterruptedException 
            {
            String geoJsonString = "";
            LatLng centerPoint = null; 
            List<LatLng> hexPoints = new ArrayList<>();
            
            if (h3 == null) {
                throw new InterruptedException("h3 failed to initialize");
            }
            
            try {
                if (h3.isValidCell(hexAddress)){
                    hexPoints = h3.cellToBoundary(hexAddress);
                    // Iterating ArrayList using Iterator
                    Iterator<LatLng> it = hexPoints.iterator();
                    int i = 0;
                    mil.nga.sf.LinearRing ring = new mil.nga.sf.LinearRing(false, false);
                    Point tpfirst = new Point();
                    while (it.hasNext()){
                        LatLng thisHex = it.next();

                        if (i < 1){
                            tpfirst =  new Point(false, false, thisHex.lat, thisHex.lng);
                            //ring.addPoint(tpfirst);
                        }
                            Point tp =  new Point(false, false, thisHex.lat, thisHex.lng);
                            ring.addPoint(tp);
                        i++;
                    } 
                    ring.addPoint(tpfirst);
                    mil.nga.sf.Polygon polygon = new mil.nga.sf.Polygon(ring);
                    geoJsonString = FeatureConverter.toStringValue(polygon);
                } else {
                    geoJsonString = "-1";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return geoJsonString;
    }

    @UserFunction(name = "com.neo4jh3.ispentagon")
    @Description("com.neo4jh3.ispentagon(hexAddress) - is hexAddress a pentagon.")
    public Boolean ispentagon(
            @Name("hexAddress") Long hexAddress) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        
        try {
            if (h3.isPentagon(hexAddress)){
                return true;
            } else {
                // throw new RuntimeException("invalid h3 resolution");
                return false;
            }
        } catch (NumberFormatException nfe) {
            // throw new RuntimeException("invalid h3 resolution");
            return false;
        }
    }

    @UserFunction(name = "com.neo4jh3.ispentagonString")
    @Description("com.neo4jh3.ispentagonString(hexAddress) - is hexAddress a pentagon.")
    public Boolean ispentagonString(
            @Name("hexAddress") String hexAddress) {
        if (h3 == null) {
            throw new RuntimeException("h3 failed to initialize");
        }
        
        try {
            if (h3.isPentagon(hexAddress)){
                return true;
            } else {
                // throw new RuntimeException("invalid h3 value");
                return false;
            }
        } catch (NumberFormatException nfe) {
            // throw new RuntimeException("invalid h3 value");
            return false;
        }
    }
    
    /* Neo4j H3 Procedures */

    @Procedure(name = "com.neo4jh3.gridDisk", mode = Mode.READ)
    @Description("CALL com.neo4jh3.gridDisk(hexAddress, ringSize)")
    public Stream<H3LongAddress> gridDisk(@Name("hexAddress") Long hexAddress, @Name("ringSize") Long ringSize)
            throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null || ringSize == null) {
            throw new InterruptedException("invalid arguments");
        }
        
        if (ringSize > 0 && h3.isValidCell(hexAddress)){
            List<Long> ringList = h3.gridDisk(hexAddress, ringSize.intValue());
            return ringList.stream().map(H3LongAddress::of);
        } else { 
            List<Long> ringList = null;
            if (ringSize < 0) {
                ringList = Collections.singletonList( -2L );
            }
            if (!h3.isValidCell(hexAddress)){
                ringList = Collections.singletonList( -1L );
            }
            return ringList.stream().map(H3LongAddress::of);
        }
    }

    @Procedure(name = "com.neo4jh3.gridDiskString", mode = Mode.READ)
    @Description("CALL com.neo4jh3.gridDiskString(hexAddress, ringSize)")
    public Stream<H3StringAddress> gridDiskString(@Name("hexAddress") String hexAddress, @Name("ringSize") Long ringSize)
            throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null || ringSize == null) {
            throw new InterruptedException("invalid arguments");
        }

        //List<Long> ringList = Collections.emptyList();
        if (ringSize > 0 && h3.isValidCell(hexAddress)){
            List<String> ringList = h3.gridDisk(hexAddress, ringSize.intValue());
            return ringList.stream().map(H3StringAddress::of);
        } else { 
            List<String> ringList = null;
            if (ringSize < 0) {
                ringList = Collections.singletonList("-2");
            }
            if (!h3.isValidCell(hexAddress)){
                ringList = Collections.singletonList("-1");
            }
            return ringList.stream().map(H3StringAddress::of);
        }
    }

    @Procedure(name = "com.neo4jh3.tochildren", mode = Mode.READ)
    @Description("CALL com.neo4jh3.tochildren(hexAddress, h3Resolution)")
    public Stream<H3LongAddress> tochildren(@Name("hexAddress") Long hexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        int h3MaxRes = h3.getResolution(hexAddress);
        
        if (h3.isValidCell(hexAddress) && h3Resolution >= h3MaxRes && h3Resolution < 16){
            return h3.cellToChildren(hexAddress, h3Resolution).stream().map(H3LongAddress::of);
        } else {
            List<Long> ringList = null;
            if (!h3.isValidCell(hexAddress)){
                ringList = Collections.singletonList(-1L);
            }
            if (h3Resolution < h3MaxRes){
                ringList = Collections.singletonList(-2L);
            }
            return ringList.stream().map(H3LongAddress::of);
        }
    }

    @Procedure(name = "com.neo4jh3.tochildrenString", mode = Mode.READ)
    @Description("CALL com.neo4jh3.tochildrenString(hexAddress, h3Resolution)")
    public Stream<H3StringAddress> tochildrenString(@Name("hexAddress") String hexAddress, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (h3 == null) {
            throw new InterruptedException("h3 failed to initialize");
        }
        if (hexAddress == null) {
            throw new InterruptedException("invalid hex address");
        }

        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        int h3MaxRes = h3.getResolution(hexAddress);
        
        if (h3.isValidCell(hexAddress) && h3Resolution >= h3MaxRes && h3Resolution < 16){
            return h3.cellToChildren(hexAddress, h3Resolution).stream().map(H3StringAddress::of);
        } else {
            List<String> ringList = null;
            if (!h3.isValidCell(hexAddress)){
                ringList = Collections.singletonList("-1");
            }
            if (h3Resolution < h3MaxRes){
                ringList = Collections.singletonList("-2");
            }
            return ringList.stream().map(H3StringAddress::of);
        }
    }

/*
     *
     * call com.neo4jh3.polygonToCellsTest(["37.7866,-122.3805","37.7198,-122.3544","37.7076,-122.5123","37.7835,-122.5247","37.8151,-122.4798"],[],7) yield value return value;
     */
    @Procedure(name = "com.neo4jh3.polygonToCells", mode = Mode.READ)
    @Description("CALL com.neo4jh3.polygonToCells(polyEdges, polyEdgeHoles, resolution, latlon order)")
    public Stream<H3LongAddress> polygonToCells(@Name("polyEdges") List<String> polyEdges, @Name("polyEdgeHoles") List<String> polyEdgeHoles, @Name("h3Res") Long h3Res, @Name("latlonorder") String latlonorder) throws InterruptedException {
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
        if (h3Resolution >= 1 && h3Resolution <= 15) {
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

            List<Long> hexList;
            if (!hexHoles.isEmpty()) {
                holesList.add(hexHoles);
                hexList = h3.polygonToCells(hexPoints, holesList, h3Resolution);
            } else {
                hexList = h3.polygonToCells(hexPoints, null, h3Resolution);
            }
            return hexList.stream().map(H3LongAddress::of);
        } else {
            List<Long> ringList = null;
            ringList = Collections.singletonList(-2L);
            return ringList.stream().map(H3LongAddress::of);
        }
    }

    /*
     *
     * call com.neo4jh3.polygonToCellsTest(["37.7866,-122.3805","37.7198,-122.3544","37.7076,-122.5123","37.7835,-122.5247","37.8151,-122.4798"],[],7) yield value return value;
     */
    @Procedure(name = "com.neo4jh3.polygonToCellsString", mode = Mode.READ)
    @Description("CALL com.neo4jh3.polygonToCellsString(polyEdges, polyEdgeHoles, resolution, latlon order)")
    public Stream<H3StringAddress> polygonToCellsString(@Name("polyEdges") List<String> polyEdges, @Name("polyEdgeHoles") List<String> polyEdgeHoles, @Name("h3Res") Long h3Res, @Name("latlonorder") String latlonorder) throws InterruptedException {
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

        if (h3Resolution >= 1 && h3Resolution <= 15) {
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
        } else {
            List<String> ringList = null;
            ringList = Collections.singletonList("-2");
            return ringList.stream().map(H3StringAddress::of);
        }
        }

        @Procedure(name = "com.neo4jh3.gridpathlatlon", mode = Mode.READ)
        @Description("CALL com.neo4jh3.gridpathlatlon(latitude, longitude, latitude, longitude, h3Resolution)")
        public Stream<H3LongAddress> gridpathlatlon(@Name("startLat") Double startLat, @Name("startLong") Double startLong, @Name("endLat") Double endLat, @Name("endLong") Double endLong, @Name("h3Res") Long h3Res) throws InterruptedException {
            if (startLat == null || startLong == null || endLat  == null || endLong == null) {
                throw new InterruptedException("invalid arguments");
            }
            if (h3 == null) {
                return Stream.empty();
            }
            List<Long> ringList = null;
            int validLatLon = 1;


            final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
            if (h3Resolution < 1 || h3Resolution > 15) {
                ringList = Collections.singletonList(-2L);
            }
            
            if ( startLat>90 || startLat<-90  || endLat>90 || endLat<-90 ){
                validLatLon = 0;
                ringList = Collections.singletonList(-3L);
            }
    
            if ( startLong>180 || startLong<-180 ||  endLong>180 || endLong<-180 ){
                validLatLon = 0;
                ringList = Collections.singletonList(-4L);
            }

            if (h3Resolution > 0 && h3Resolution <= 15 && validLatLon > 0){
                final String starthexAddr = h3.latLngToCellAddress(startLat, startLong, h3Resolution);
                final String endhexAddr = h3.latLngToCellAddress(endLat, endLong, h3Resolution);
                return h3.gridPathCells(starthexAddr, endhexAddr)
                    .stream()
                    .map(H3LongAddress::of);
            } else {
                return ringList.stream().map(H3LongAddress::of);
            }
        }

    @Procedure(name = "com.neo4jh3.gridpathlatlonString", mode = Mode.READ)
    @Description("CALL com.neo4jh3.gridpathlatlonString(latitude, longitude, latitude, longitude, h3Resolution)")
    public Stream<H3StringAddress> gridpathlatlonString(@Name("startLat") Double startLat, @Name("startLong") Double startLong, @Name("endLat") Double endLat, @Name("endLong") Double endLong, @Name("h3Res") Long h3Res) throws InterruptedException {
        if (startLat == null || startLong == null || endLat  == null || endLong == null) {
            throw new InterruptedException("invalid arguments");
        }
        if (h3 == null) {
            return Stream.empty();
        }

        List<String> ringList = null;
        int validLatLon = 1;


        final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
        if (h3Resolution < 1 || h3Resolution > 15) {
            ringList = Collections.singletonList("-2");
        }
            
        if ( startLat>90 || startLat<-90  || endLat>90 || endLat<-90 ){
            validLatLon = 0;
            ringList = Collections.singletonList("-3");
        }
    
        if ( startLong>180 || startLong<-180 ||  endLong>180 || endLong<-180 ){
            validLatLon = 0;
            ringList = Collections.singletonList("-4");
        }

        if (h3Resolution > 0 && h3Resolution <= 15 && validLatLon > 0){
            final String starthexAddr = h3.latLngToCellAddress(startLat, startLong, h3Resolution);
            final String endhexAddr = h3.latLngToCellAddress(endLat, endLong, h3Resolution);
            return h3.gridPathCells(starthexAddr, endhexAddr)
                .stream()
                .map(H3StringAddress::of);
        } else {
            return ringList.stream().map(H3StringAddress::of);
        }
    }


    @Procedure(name = "com.neo4jh3.gridpathcell", mode = Mode.READ)
    @Description("CALL com.neo4jh3.gridpathcell(fromAddress, toAddress)")
    public Stream<H3LongAddress> gridpathcell(@Name("fromAddress") Long fromAddress, @Name("toAddress") Long toAddress) throws InterruptedException {
        if (fromAddress == null || toAddress == null) {
            throw new InterruptedException("invalid arguments");
        }
        if (h3 == null) {
            return Stream.empty();
        }

        List<String> ringList = null;
        
        if (h3.isValidCell(fromAddress) && h3.isValidCell(toAddress)){
            return h3.gridPathCells(fromAddress, toAddress)
                .stream()
                .map(H3LongAddress::of);
        } else {
            ringList = Collections.singletonList("-1");
            return ringList.stream().map(H3LongAddress::of);
        }
    }

    @Procedure(name = "com.neo4jh3.gridpathcellString", mode = Mode.READ)
    @Description("CALL com.neo4jh3.gridpathcellString(fromAddress, toAddress, h3Resolution)")
    public Stream<H3StringAddress> gridpathcellString(@Name("fromAddress") String fromAddress, @Name("toAddress") String toAddress) throws InterruptedException {
        if (fromAddress == null || toAddress == null) {
            throw new InterruptedException("invalid arguments");
        }
        if (h3 == null) {
            return Stream.empty();
        }

        List<String> ringList = null;
        
        if (h3.isValidCell(fromAddress) && h3.isValidCell(toAddress)){
            return h3.gridPathCells(fromAddress, toAddress)
                .stream()
                .map(H3StringAddress::of);
        } else {
            ringList = Collections.singletonList("-1");
            return ringList.stream().map(H3StringAddress::of);
        }
    }
    
    /*
     * Return list of hex addresses for a line
     */
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

// Compact
@Procedure(name = "com.neo4jh3.compactString", mode = Mode.READ)
@Description("CALL com.neo4jh3.compactString(listCells)")
public Stream<H3StringAddress> compactString(@Name("listCells") List<String> listCells) throws InterruptedException {
    if (listCells == null) {
        throw new InterruptedException("invalid list of hex addresses");
    }
    if (h3 == null) {
        return Stream.empty();
    }
    return h3.compactCellAddresses(listCells)
    .stream()
    .map(H3StringAddress::of);
    
}

// Compact
@Procedure(name = "com.neo4jh3.compact", mode = Mode.READ)
@Description("CALL com.neo4jh3.compact(listCells)")
public Stream<H3LongAddress> compact(@Name("listCells") List<Long> listCells) throws InterruptedException {
    if (listCells == null) {
        throw new InterruptedException("invalid list of hex addresses");
    }
    if (h3 == null) {
        return Stream.empty();
    }
    return h3.compactCells(listCells)
    .stream()
    .map(H3LongAddress::of);
    
}

// Uncompact
@Procedure(name = "com.neo4jh3.uncompact", mode = Mode.READ)
@Description("CALL com.neo4jh3.uncompact(listCells, h3Resolution)")
public Stream<H3LongAddress> unCompact(@Name("listCells") List<Long> listCells, @Name("h3Res") Long h3Res) throws InterruptedException {
    if (listCells == null) {
        throw new InterruptedException("invalid list of hex addresses");
    }
    if (h3 == null) {
        return Stream.empty();
    }

    final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
    if (h3Resolution > 1 && h3Resolution < 15) {   
        Integer maxRes = 0;
        Integer curRes = 0;

        ListIterator<Long> iterator = listCells.listIterator();
        while (iterator.hasNext()) {
            curRes = h3.getResolution(iterator.next());
            if (curRes.compareTo(maxRes)>0){
                maxRes = curRes;
            }
        }
        if (h3Resolution >= maxRes){
            return h3.uncompactCells(listCells,h3Resolution)
            .stream()
            .map(H3LongAddress::of);
        } else {
            List<Long> ringList = null;
            ringList = Collections.singletonList(-2L);
            return ringList.stream().map(H3LongAddress::of);
        }
    } else {
        List<Long> ringList = null;
        ringList = Collections.singletonList(-2L);
        return ringList.stream().map(H3LongAddress::of);
    }
    
}

// Uncompact
@Procedure(name = "com.neo4jh3.uncompactString", mode = Mode.READ)
@Description("CALL com.neo4jh3.uncompactString(listCells, h3Resolution)")
public Stream<H3StringAddress> uncompactString(@Name("listCells") List<String> listCells, @Name("h3Res") Long h3Res) throws InterruptedException {
    if (listCells == null) {
        throw new InterruptedException("invalid list of hex addresses");
    }
    if (h3 == null) {
        return Stream.empty();
    }

    final int h3Resolution = h3Res == null ? DEFAULT_H3_RESOLUTION : h3Res.intValue();
    if (h3Resolution > 1 && h3Resolution < 15) {   
        Integer maxRes = 0;
        Integer curRes = 0;

        ListIterator<String> iterator = listCells.listIterator();
        while (iterator.hasNext()) {
            curRes = h3.getResolution(iterator.next());
            if (curRes.compareTo(maxRes)>0){
                maxRes = curRes;
            }
        }
        if (h3Resolution >= maxRes){
            return h3.uncompactCellAddresses(listCells,h3Resolution)
            .stream()
            .map(H3StringAddress::of);
        } else {
            List<String> ringList = null;
            ringList = Collections.singletonList("-2");
            return ringList.stream().map(H3StringAddress::of);
        }
    } else {
        List<String> ringList = null;
        ringList = Collections.singletonList("-2");
        return ringList.stream().map(H3StringAddress::of);
    }
}

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
