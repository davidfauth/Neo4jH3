# Documentation
## com.neo4jh3.boundaryaswkt( h3CellIdExpr )
Returns the polygonal boundary of the input H3 cell in WKT format.

### Syntax
RETURN com.neo4jh3.boundaryaswkt( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A LONG expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.boundaryaswkt(599686042433355775) AS value
    POLYGON ((37.2713558667319 -121.91508032705622, 37.353926450852256 -121.86222328902491, 37.42834118609435 -121.92354999630156, 37.42012867767778 -122.03773496427027, 37.33755608435298 -122.09042892904397, 37.26319797461824 -122.02910130918998, 37.2713558667319 -121.91508032705622))
       
    RETURN com.neo4jh3.boundaryaswkt(1234) AS value
    -1

## com.neo4jh3.boundaryaswktString( h3CellIdExpr )
Returns the polygonal boundary of the input H3 cell in WKT format.

### Syntax
RETURN com.neo4jh3.boundaryaswktString( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.boundaryaswktString('8009fffffffffff') AS value
    POLYGON ((63.09505407752544 -10.444977544778336, 55.706768465152265 5.523646549290317, 58.40154487035269 25.082722326707884, 68.92995788193983 31.831280499087388, 73.31022368544396 0.3256103519432604, 63.09505407752544 -10.444977544778336))
       
    RETURN com.neo4jh3.boundaryaswktString('1234') AS value
    -1

## com.neo4jh3.boundaryaswkb( h3CellIdExpr )
Returns the polygonal boundary of the input H3 cell in WKB format.

### Syntax
RETURN com.neo4jh3.boundaryaswkb( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A LONG expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.boundaryaswkb(599686042433355775) AS value
    000000000300000001000000074042A2BBC9FE987BC05E7A90AD137AD84042AD4D7641CCC6C05E772EAA970D8A4042B6D3E24CE70DC05E7B1B717195834042B5C6C6C95E70C05E826A3FE95D384042AB3509AB6E52C05E85C9966B36CB4042A1B078A2ADECC05E81DCCBBCCF794042A2BBC9FE987BC05E7A90AD137AD8
       
    RETURN com.neo4jh3.boundaryaswkb(1234) AS value
    -1

## com.neo4jh3.boundaryaswkbString( h3CellIdExpr )
Returns the polygonal boundary of the input H3 cell in WKB format.

### Syntax
RETURN com.neo4jh3.boundaryaswkbString( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.boundaryaswkbString('8009fffffffffff') AS value
    00000000030000000100000006404F8C2ABB65295FC024E3D418C48DFE404BDA776399D62840161836CD0F75ED404D3365D283054B4039152D4A57DC0140513B846E1065B2403FD4CECC7D6205405253DAB471DB4A3FD4D6CCCD35766F404F8C2ABB65295FC024E3D418C48DFE
       
    RETURN com.neo4jh3.boundaryaswkbString('1234') AS value
    -1
    
## com.neo4jh3.boundaryasgeojson( h3CellIdExpr )
Returns the polygonal boundary of the input H3 cell in GeoJSON format.

### Syntax
RETURN com.neo4jh3.boundaryasgeojson( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A LONG expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.boundaryasgeojson(599686042433355775) AS value
    {"type":"Polygon","coordinates":[[[-121.91508032706,37.271355866732],[-121.86222328902,37.353926450852],[-121.9235499963,37.428341186094],[-122.03773496427 37.420128677678],[-122.09042892904,37.337556084353],[-122.02910130919,37.263197974618],[-121.91508032706,37.271355866732]]]}
       
    RETURN com.neo4jh3.boundaryasgeojson(1234) AS value
    -1

## com.neo4jh3.boundaryasgeojsonString( h3CellIdExpr )
Returns the polygonal boundary of the input H3 cell in GeoJSON format.

### Syntax
RETURN com.neo4jh3.boundaryasgeojsonString( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.boundaryasgeojsonString('8009fffffffffff') AS value
    {"type":"Polygon","coordinates":[[[-10.444977544778,63.095054077525],[5.5236465492903,55.706768465152],[25.082722326708,58.401544870353],[31.831280499087,68.92995788194],[0.32561035194326,73.310223685444],[-10.444977544778,63.095054077525]]]}  
         
    RETURN com.neo4jh3.boundaryasgeojsonString('1234') AS value
    -1
    
## com.neo4jh3.toparent( h3CellIdExpr, resolutionExpr )
Returns the parent H3 cell of the input H3 cell at the specified resolution.

### Syntax
RETURN com.neo4jh3.toparent( h3CellIdExpr, resolutionExpr ) AS value;

### Arguments
* h3CellId1Expr: A hexadecimal LONG expression representing an H3 cell ID.
* resolutionExpr: A LONG expression, whose value is expected to be between 0 and h3_resolution(h3CellIdExpr) inclusive, specifying the resolution of the parent H3 cell ID.

### Returns
A LONG value of the h3CellIdExpr expression, corresponding to the parent H3 cell ID of the input H3 cell at the specified resolution.

### Error conditions
If resolutionExpr is smaller than 0 or larger than h3_resolution(h3CellIdExpr), the function returns -1.
If h3CellIdExpr is an invalid H3 cell, the function returns -2.

### Example
    RETURN com.neo4jh3.toparent(604197150066212863, 3) AS value
    590686371182542847
    
    RETURN com.neo4jh3.toparent(604197150066212863, 23) AS value
    -1
    
    RETURN com.neo4jh3.toparent(12345, 6) AS value
    -2

## com.neo4jh3.toparentString( h3CellIdExpr, resolutionExpr )
Returns the parent H3 cell of the input H3 cell at the specified resolution.

### Syntax
RETURN com.neo4jh3.toparentString( h3CellIdExpr, resolutionExpr ) AS value;

### Arguments
* h3CellIdExpr: A hexadecimal STRING expression representing an H3 cell ID.
* resolutionExpr: A LONG expression, whose value is expected to be between 0 and h3_resolution(h3CellIdExpr) inclusive, specifying the resolution of the parent H3 cell ID.

### Returns
A STRING value of the h3CellIdExpr expression, corresponding to the parent H3 cell ID of the input H3 cell at the specified resolution.

### Error conditions
If resolutionExpr is smaller than 0 or larger than h3_resolution(h3CellIdExpr), the function returns -1.
If h3CellIdExpr is an invalid H3 cell, the function returns -2.

### Example
    RETURN com.neo4jh3.toparentString('892830926cfffff', 6) AS value
    862830927ffffff
    
    RETURN com.neo4jh3.toparentString('892830926cfffff', 16) AS value
    -1
    
    RETURN com.neo4jh3.toparentString('1234', 6) AS value
    -2
    

## com.neo4jh3.cellToLatLng( h3CellId1Expr )
Returns the center latitude and longitude of the input H3 cell.

### Syntax
RETURN com.neo4jh3.cellToLatLng( h3CellId1Expr ) AS value;

### Arguments
* h3CellId1Expr: A hexadecimal LONG expression representing an H3 cell ID.

### Returns
A STRING value consisting of the latitude and longitude of the h3CellIdExpr.

### Error conditions
If h3XellIdExpr is an invalid h3 address, the function returns -1.

### Example
    RETURN com.neo4jh3.cellToLatLng(635714569676958015) AS value
    37.81989535912348,-122.47829651373911
    
    RETURN com.neo4jh3.cellToLatLng(123) AS value
    -1
    
## com.neo4jh3.cellToLatLngString( h3CellIdExpr )
Returns the center latitude and longitude of the input H3 cell.

### Syntax
RETURN com.neo4jh3.cellToLatLng( h3CellIdExpr ) AS value;

### Arguments
* h3CellId1Expr: A hexadecimal STRING expression representing an H3 cell ID.

### Returns
A STRING value consisting of the latitude and longitude of the h3CellIdExpr.

### Error conditions
If h3XellIdExpr is an invalid h3 address, the function returns -1.

### Example
    RETURN com.neo4jh3.cellToLatLngString('892830926cfffff') AS value
    37.56424780593244,-122.3253058831214
    
    RETURN com.neo4jh3.cellToLatLngString('notavalidhex') AS value
    -1
    
## com.neo4jh3.centerasgeojson( h3CellIdExpr )
Returns the center of the input H3 cell as a point in GeoJSON format.

### Syntax
RETURN com.neo4jh3.centerasgeojson( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A LONG expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.centerasgeojson(599686042433355775) AS value
   {"type":"Point","coordinates":[-121.97637597255,37.345793375368]}
    
    RETURN com.neo4jh3.centerasgeojson(1234) AS value
    -1
    
## com.neo4jh3.centerasgeojsonString( h3CellIdExpr )
Returns the center of the input H3 cell as a point in GeoJSON format.

### Syntax
RETURN com.neo4jh3.centerasgeojsonString( h3CellIdExpr ) AS value


### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.centerasgeojsonString('8009fffffffffff') AS value
   {"type":"Point","coordinates":[64.70000013,10.53619908]}
    
    RETURN com.neo4jh3.centerasgeojsonString(1234) AS value
    -1
     
## com.neo4jh3.centeraswkt( h3CellIdExpr )
Returns the center of the input H3 cell as a point in WKT format.

### Syntax
RETURN com.neo4jh3.centeraswkt( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A LONG expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.centeraswkt(599686042433355775) AS value
    POINT ( 37.34579337536848 -121.9763759725512 )
       
    RETURN com.neo4jh3.centeraswkt(1234) AS value
    -1

## com.neo4jh3.centeraswktString( h3CellIdExpr )
Returns the center of the input H3 cell as a point in WKT format.

### Syntax
RETURN com.neo4jh3.centeraswktString( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.centeraswktString('8009fffffffffff') AS value
    POINT ( 64.70000012793487 10.53619907546767 )
       
    RETURN com.neo4jh3.centeraswktString('1234') AS value
    -1
    
## com.neo4jh3.centeraswkb( h3CellIdExpr )
Returns the center of the input H3 cell as a point in WKB format.

### Syntax
RETURN com.neo4jh3.centeraswkb( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A LONG expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.centeraswkb(599686042433355775) AS value
    00000000014042AC42F51330C7C05E7E7CF1A5AD49
           
    RETURN com.neo4jh3.centeraswkb(1234) AS value
    -1

## com.neo4jh3.centeraswkbString( h3CellIdExpr )
Returns the center of the input H3 cell as a point in WKB format.

### Syntax
RETURN com.neo4jh3.centeraswktString( h3CellIdExpr ) as value

### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.

### Returns
A value of the type STRING representing the center of the input H3 cell as a point in GeoJSON format.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    RETURN com.neo4jh3.centeraswkbString('8009fffffffffff') AS value
    000000000140502CCCCD562B4540251288AF6A8EE3
           
    RETURN com.neo4jh3.centeraswkbString('1234') AS value
    -1
    
## com.neo4jh3.distanceBetweenHexes( h3CellId1Expr, h3CellId2Expr )
Returns the great circle distance in KM between two valid H3 addresses.

### Syntax
RETURN com.neo4jh3.distanceBetweenHexes( h3CellId1Expr, h3CellId2Expr ) AS value;

### Arguments
* h3CellId1Expr: A hexadecimal LONG expression representing an H3 cell ID.
* h3CellId2Expr: A hexadecimal LONG expression representing an H3 cell ID.

### Returns
A DOUBLE value consisting of the great circle distance in KM between h3CellId1Expr and h3CellId2Expr

### Error conditions
If h3CellId1Expr or h3CellId2Expr is an invalid h3 address, the function returns -1.0.

### Example
    RETURN com.neo4jh3.distanceBetweenHexes(599686042433355775,599686015589810175) AS value
    17.870163466857925
    
    RETURN com.neo4jh3.distanceBetweenHexes(3111,599686015589810175) AS value
    -1.0

## com.neo4jh3.distanceBetweenHexesString( h3CellId1Expr, h3CellId2Expr )
Returns the great circle distance in KM between two valid H3 addresses.

### Syntax
RETURN com.neo4jh3.distanceBetweenHexes( h3CellId1Expr, h3CellId2Expr ) AS value;

### Arguments
* h3CellId1Expr: A hexadecimal STRING expression representing an H3 cell ID.
* h3CellId2Expr: A hexadecimal STRING expression representing an H3 cell ID.

### Returns
A DOUBLE value consisting of the great circle distance in KM between h3CellId1Expr and h3CellId2Expr

### Error conditions
If h3CellId1Expr or h3CellId2Expr is an invalid h3 address, the function returns -1.0.

### Example
    RETURN com.neo4jh3.distanceBetweenHexesString('8a2989352777fff','8a498935223ffff') AS value
    2360.8203881920604
    
    RETURN com.neo4jh3.distanceBetweenHexesString('123','8a498935223ffff') AS value
    -1.0

## com.neo4jh3.gridDistance( h3CellId1Expr, h3CellId2Expr )
Returns the grid distance of the two input H3 cells, that are expected to have the same resolution.

### Syntax
RETURN com.neo4jh3.gridDistance( h3CellId1Expr, h3CellId2Expr ) AS value;

### Arguments
* h3CellId1Expr: A hexadecimal LONG expression representing an H3 cell ID.
* h3CellId2Expr: A hexadecimal LONG expression representing an H3 cell ID.

### Returns
A LONG value that is the grid distance of the two input H3 cells, that are expected to have the same resolution.

### Error conditions
If h3CellId1Expr or h3CellId2Expr is not a valid H3 cell ID, the function returns -1.

### Example
    RETURN com.neo4jh3.gridDistance(599686030622195711,599686015589810175) as value
    2
    
    RETURN com.neo4jh3.gridDistance(1234,599686015589810175) as value
    -1
    
## com.neo4jh3.gridDistanceString( h3CellId1Expr, h3CellId2Expr )
Returns the grid distance of the two input H3 cells, that are expected to have the same resolution.

### Syntax
RETURN com.neo4jh3. gridDistance( h3CellId1Expr, h3CellId2Expr ) AS value;

### Arguments
* h3CellId1Expr: A hexadecimal STRING expression representing an H3 cell ID.
* h3CellId2Expr: A hexadecimal STRING expression representing an H3 cell ID.

### Returns
A LONG value that is the grid distance of the two input H3 cells, that are expected to have the same resolution.

### Error conditions
If h3CellId1Expr or h3CellId2Expr is not a valid H3 cell ID, the function returns "-1".

### Example
    RETURN com.neo4jh3.gridDistanceString('85283473fffffff','8528342bfffffff') as value
    2
    
    RETURN com.neo4jh3.gridDistanceString('12111','8528342bfffffff') as value
    -1

## com.neo4jh3.h3HexAddress( latitude, longitude, resolution )
Returns the H3 cell ID (as a LONG) corresponding to the provided longitude and latitude at the specified resolution.

### Syntax
RETURN com.neo4jh3.h3HexAddress( latitudeExpr, longitudeExpr, resolutionExpr ) AS value;

### Arguments
* latitudeExpr: A DOUBLE expression representing the latitude (in degrees) of the location whose H3 cell ID we want to compute.
* longitudeExpr: A DOUBLE expression representing the longitude (in degrees) of the location whose H3 cell ID we want to compute.
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution for the H3 cell ID.

### Returns
A value of the type of LONG representing, as a hexadecimal string, the H3 cell ID of the input location at the specified resolution.

### Error conditions
* If resolutionExpr is not a valid H3 cell ID, the function returns -2.
* If latitudeExpr is not a valid latitude the function returns -3.
* If longitudeExpr is not a valid longitude, the function returns -4.

### Example
    RETURN com.neo4jh3.h3HexAddress( 37.8199, -122.4783, 13) AS value
    635714569676958015
    
     RETURN com.neo4jh3.h3HexAddress( 37.8199, -122.4783, 16) AS value
    -2
    
    RETURN com.neo4jh3.h3HexAddress( 97.8199, -122.4783, 13) AS value
    -3
        
    
## com.neo4jh3.h3HexAddressString( latitude, longitude, resolution )
Returns the H3 cell ID (as a hexadecimal STRING) corresponding to the provided longitude and latitude at the specified resolution.

### Syntax
RETURN com.neo4jh3.h3HexAddressString( latitudeExpr, longitudeExpr, resolutionExpr ) AS value;

### Arguments
* latitudeExpr: A DOUBLE expression representing the latitude (in degrees) of the location whose H3 cell ID we want to compute.
* longitudeExpr: A DOUBLE expression representing the longitude (in degrees) of the location whose H3 cell ID we want to compute.
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution for the H3 cell ID.

### Returns
A value of the type of STRING representing, as a hexadecimal string, the H3 cell ID of the input location at the specified resolution.

### Error conditions
* If resolutionExpr is not a valid H3 cell ID, the function returns -2.
* If latitudeExpr is not a valid latitude the function returns -3.
* If longitudeExpr is not a valid longitude, the function returns -4.

### Example
    RETURN com.neo4jh3.h3HexAddressString( 37.8199, -122.4783, 13) AS value
    8d283087022a93f
    
    RETURN com.neo4jh3.h3HexAddressString( 37.8199, -122.4783, 16) AS value
    -2
    
    RETURN com.neo4jh3.h3HexAddressString( 97.8199, -122.4783, 13) AS value
    -3
        
## com.neo4jh3.h3tostring( h3CellIdExpr )
Converts the input H3 cell ID to its equivalent hexadecimal string representation.

### Syntax
RETURN com.neo4jh3.h3tostring( h3CellIdExpr ) AS value;

### Arguments
* h3CellIdExpr: A LONG expression representing an H3 cell ID.

### Returns
A value of type STRING. The function converts the LONG to its corresponding hexadecimal string. 


### Error conditions
If h3CellIdExpr is not a valid H3 cell ID, the function returns "-1".

### Example
    RETURN com.neo4jh3.h3tostring(599686042433355775) AS value
    85283473fffffff
        
    RETURN com.neo4jh3.h3tostring(0) AS value
    -1

## com.neo4jh3.h3Resolution( h3CellIdExpr )
Returns the resolution of the input H3 cell.

### Syntax
RETURN com.neo4jh3.h3Resolution( h3CellIdExpr ) AS value;

### Arguments
* h3CellIdExpr: A LONG expression representing an H3 cell ID.

### Returns
A value of whose type LONG between 0 and 15, that is the resolution of the input H3 cell ID.


### Error conditions
If h3CellIdExpr is not a valid H3 cell ID, the function returns -1.

### Example
    RETURN com.neo4jh3.h3Resolution(599686042433355775) AS value
    5
        
    RETURN com.neo4jh3.h3Resolution(337) AS value
    -1
    
## com.neo4jh3.h3ResolutionString( h3CellIdExpr )
Returns the resolution of the input H3 cell.

### Syntax
RETURN com.neo4jh3.h3ResolutionString( h3CellIdExpr ) AS value;

### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.

### Returns
A value of whose type LONG between 0 and 15, that is the resolution of the input H3 cell ID.


### Error conditions
If h3CellIdExpr is not a valid H3 cell ID, the function returns -1.

### Example
    RETURN com.neo4jh3.h3ResolutionString('85283473fffffff') AS value
    5
        
    RETURN com.neo4jh3.h3ResolutionString('notavalidhexaddress') AS value
    -1

## com.neo4jh3.h3Validate( h3CellIdExpr )
Returns the input value, that is of type LONG if it corresponds to a valid H3 cell ID, or emits an error otherwise.

### Syntax
RETURN com.neo4jh3.h3Validate( h3CellIdExpr) AS value;

### Arguments
h3CellIdExpr: A LONG expression that is expected to represent a valid H3 cell ID.

### Error conditions
If h3CellIdExpr is not a valid H3 cell ID, the function returns 0.

### Example
    RETURN com.neo4jh3.h3Validate(590112357393367039) AS value;
    590112357393367039
    
    RETURN com.neo4jh3.h3Validate(123411) AS value;
    -1
    
## com.neo4jh3.ispentagon( h3CellIdExpr )
Returns true if the input LONG corresponds to a pentagonal H3 cell or not.

### Syntax
RETURN com.neo4jh3.ispentagon(h3CellIdExpr) AS value;

### Arguments
h3CellIdExpr: A LONG expression that is expected to represent a valid H3 cell ID.

### Error conditions
If h3CellIdExpr is not a valid H3 cell ID, the function returns false;

### Example
    RETURN com.neo4jh3.ispentagon(590112357393367039) AS value;
    true
    
    RETURN com.neo4jh3.ispentagon(123456) AS value;
    false

## com.neo4jh3.ispentagonString( h3CellIdExpr )
Returns true if the input STRING corresponds to a pentagonal H3 cell or not.

### Syntax
RETURN com.neo4jh3.ispentagonString(h3CellIdExpr) AS value;

### Arguments
h3CellIdExpr: A STRING expression that is expected to represent a valid H3 cell ID.

### Error conditions
If h3CellIdExpr is not a valid H3 cell ID, the function returns false;

### Example
    
    RETURN com.neo4jh3.ispentagonString('85283473fffffff') AS value;
    false

## com.neo4jh3.h3ValidateString( h3CellIdExpr )
Returns the input value, that is of type STRING if it corresponds to a valid H3 cell ID, or emits an error otherwise.

### Syntax
RETURN com.neo4jh3.h3ValidateString(h3CellIdExpr) AS value;

### Arguments
h3CellIdExpr: A STRING expression that is expected to represent a valid H3 cell ID.

### Error conditions
If h3CellIdExpr is not a valid H3 cell ID, the function returns "invalid h3 resolution".

### Example
    RETURN com.neo4jh3.h3ValidateString('85283473fffffff') AS value;
    85283473fffffff
    
    RETURN com.neo4jh3.h3ValidateString('notvalidstring') AS value;
    -1
    

## com.neo4jh3.latlongash3( latitude, longitude, resolution )
Returns the H3 cell ID (as a LONG) corresponding to the provided longitude and latitude at the specified resolution.

### Syntax
RETURN com.neo4jh3.latlongash3( latitudeExpr, longitudeExpr, resolutionExpr ) AS value;

### Arguments
* latitudeExpr: A DOUBLE expression representing the latitude (in degrees) of the location whose H3 cell ID we want to compute.
* longitudeExpr: A DOUBLE expression representing the longitude (in degrees) of the location whose H3 cell ID we want to compute.
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution for the H3 cell ID.

### Returns
A value of the type of LONG representing, as a hexadecimal string, the H3 cell ID of the input location at the specified resolution.

### Error conditions
* If resolutionExpr is not a valid H3 cell ID, the function returns -2.
* If latitudeExpr is not a valid latitude the function returns -3.
* If longitudeExpr is not a valid longitude, the function returns -4.


### Example
    RETURN com.neo4jh3.latlongash3( 37.8199, -122.4783, 13) AS value
    635714569676958015
    
    RETURN com.neo4jh3.latlongash3( 37.8199, -122.4783, 16) AS value
    -2

	RETURN com.neo4jh3.latlongash3( 97.8199, -122.4783, 13) AS value
    -3
    
    RETURN com.neo4jh3.latlongash3( 67.8199, -222.4783, 13) AS value
    -4
     
## com.neo4jh3.latlongash3String( latitude, longitude, resolution )
Returns the H3 cell ID (as a hexadecimal STRING) corresponding to the provided longitude and latitude at the specified resolution.

### Syntax
RETURN com.neo4jh3.latlongash3String( latitudeExpr, longitudeExpr, resolutionExpr ) AS value;

### Arguments
* latitudeExpr: A DOUBLE expression representing the latitude (in degrees) of the location whose H3 cell ID we want to compute.
* longitudeExpr: A DOUBLE expression representing the longitude (in degrees) of the location whose H3 cell ID we want to compute.
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution for the H3 cell ID.

### Returns
A value of the type of STRING representing, as a hexadecimal string, the H3 cell ID of the input location at the specified resolution.

### Error conditions
* If resolutionExpr is not a valid H3 cell ID, the function returns -2.
* If latitudeExpr is not a valid latitude the function returns -3.
* If longitudeExpr is not a valid longitude, the function returns -4.
* 
### Example
    RETURN com.neo4jh3.latlongash3String( 37.8199, -122.4783, 13) AS value
    8d283087022a93f
    
    RETURN com.neo4jh3.latlongash3String( 37.8199, -122.4783, 16) AS value
    -2
    
    RETURN com.neo4jh3.latlongash3String( 97.8199, -222.4783, 13) AS value
    -3
    
    RETURN com.neo4jh3.latlongash3String( 97.8199, -222.4783, 13) AS value
    -4

## com.neo4jh3.lineash3( geographyExpr, resolutionExpr )
Returns the H3 cell ID (as a LONG) corresponding to the provided LINESTRING at the specified resolution.

### Syntax
RETURN com.neo4jh3.lineash3( geographyExpr, resolutionExpr ) 

### Arguments
* geographyExpr: A STRING expression representing a LINESTRING geography in WKT format
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
Returns the H3 cell ID (as a LONG) corresponding to the provided point at the specified resolution.

### Error conditions
If geographyExpr is of type STRING and the value is either an invalid WKT or does not represent a point, the function returns -1

If resolutionExpr is smaller than 0 or larger than 15, the function returns -2

### Example
    RETURN com.neo4jh3.lineash3('LINESTRING((37.2713558667319 -121.91508032705622), (37.353926450852256 -121.86222328902491))',13) AS value
    635714810904422079
    
    RETURN com.neo4jh3.lineash3('zzz(37.8199 -122.4783)',13) AS value
    -1
    
    RETURN com.neo4jh3.lineash3('LINESTRING((37.2713558667319 -121.91508032705622), (37.353926450852256 -121.86222328902491))',16) AS value
    -2

## com.neo4jh3.lineash3String( geographyExpr, resolutionExpr )
Returns the H3 cell ID (as a STRING) corresponding to the provided LINESTRING at the specified resolution.

### Syntax
RETURN com.neo4jh3.lineash3String( geographyExpr, resolutionExpr ) 

### Arguments
* geographyExpr: A STRING expression representing a LINESTRING geography in WKT format
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
Returns the H3 cell ID (as a LONG) corresponding to the provided LINESTRING at the specified resolution.

### Error conditions
If geographyExpr is of type STRING and the value is either an invalid WKT or does not represent a point, the function returns -1

If resolutionExpr is smaller than 0 or larger than 15, the function returns -2

### Example
    RETURN com.neo4jh3.lineash3String('LINESTRING((37.2713558667319 -121.91508032705622), (37.353926450852256 -121.86222328902491))',13) AS value
    8d283409a69a6bf
    
    RETURN com.neo4jh3.lineash3String('zzz(37.8199 -122.4783)',13) AS value
    -1
    
    RETURN com.neo4jh3.lineash3String('LINESTRING((37.2713558667319 -121.91508032705622), (37.353926450852256 -121.86222328902491))',16) AS value
    -2

## com.neo4jh3.maxChild( h3CellIdExpr, resolutionExpr )
Returns the child of minimum value of the input H3 cell at the specified resolution.

### Syntax
RETURN com.neo4jh3.maxChild( h3CellIdExpr, resolutionExpr ) AS value

### Arguments
* h3CellId1Expr: A hexadecimal LONG expression representing an H3 cell ID.
* resolutionExpr: An INT expression, whose value is expected to be between h3_resolution(h3CellIdExpr) and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
A value of the same type as the type of the h3CellIdExpr expression, corresponding to the child of maximum value of the input H3 cell ID at the specified resolution.

### Error conditions
If h3CellIdExpr is an invalid h3 address, the function returns -1.
If resolutionExpr is an invalid h3 resolution, the function returns -2.

### Example
    RETURN com.neo4jh3.maxChild(599686042433355775, 10) AS value
    622204040416821247
    
    RETURN com.neo4jh3.maxChild(123,10) AS value
    -1
    
    RETURN com.neo4jh3.maxChild(599686042433355775,23) AS value
    -2
    
## com.neo4jh3.maxChildString( h3CellIdExpr, resolutionExpr )
Returns the child of maximum value of the input H3 cell at the specified resolution.

### Syntax
RETURN com.neo4jh3.maxChild( h3CellIdExpr, resolutionExpr ) AS value

### Arguments
* h3CellId1Expr: A hexadecimal STRING expression representing an H3 cell ID.
* resolutionExpr: An INT expression, whose value is expected to be between h3_resolution(h3CellIdExpr) and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
A value of the same type as the type of the h3CellIdExpr expression, corresponding to the child of maximum value of the input H3 cell ID at the specified resolution.

### Error conditions
If h3CellIdExpr is an invalid h3 address, the function returns -1.
If resolutionExpr is an invalid h3 resolution, the function returns -2.

### Example
    RETURN com.neo4jh3.maxChildString('85283473fffffff', 10) AS value
    8a2834736db7fff
    
    RETURN com.neo4jh3.maxChildString('123',10) AS value
    -1
    
    RETURN com.neo4jh3.maxChildString('85283473fffffff',27) AS value
    -2
    
## com.neo4jh3.minChild( h3CellIdExpr, resolutionExpr )
Returns the child of minimum value of the input H3 cell at the specified resolution.

### Syntax
RETURN com.neo4jh3.minChild( h3CellIdExpr, resolutionExpr ) AS value

### Arguments
* h3CellId1Expr: A hexadecimal LONG expression representing an H3 cell ID.
* resolutionExpr: An INT expression, whose value is expected to be between h3_resolution(h3CellIdExpr) and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
A value of the same type as the type of the h3CellIdExpr expression, corresponding to the child of minimum value of the input H3 cell ID at the specified resolution.

### Error conditions
If h3CellIdExpr is an invalid h3 address, the function returns -1.
If resolutionExpr is an i nvalid resolution or smaller than h3_resolution(h3CellIdExpr), the function returns -2.

### Example
    RETURN com.neo4jh3.minChild(599686042433355775, 10) AS value
    622204039496499199
    
    RETURN com.neo4jh3.minChild(123,10) AS value
    -1
    
     RETURN com.neo4jh3.minChild(599686042433355775,23) AS value
    -2
    
## com.neo4jh3.minChildString( h3CellIdExpr, resolutionExpr )
Returns the child of minimum value of the input H3 cell at the specified resolution.

### Syntax
RETURN com.neo4jh3.minChild( h3CellIdExpr, resolutionExpr ) AS value

### Arguments
* h3CellId1Expr: A hexadecimal STRING expression representing an H3 cell ID.
* resolutionExpr: An INT expression, whose value is expected to be between h3_resolution(h3CellIdExpr) and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
A value of the same type as the type of the h3CellIdExpr expression, corresponding to the child of minimum value of the input H3 cell ID at the specified resolution.

### Error conditions
If h3CellIdExpr is an invalid h3 address, the function returns -1.
If resolutionExpr is an invalid resolution or smaller than h3_resolution(h3CellIdExpr), the function returns -2.

### Example
    RETURN com.neo4jh3.minChildString('85283473fffffff', 10) AS value
    8a2834700007fff
    
    RETURN com.neo4jh3.minChildString('123',10) AS value
    -1
    
    RETURN com.neo4jh3.minChildString('85283473fffffff',27) AS value
    -2

## com.neo4jh3.multilineash3( geographyExpr, resolutionExpr )
Returns the H3 cell ID (as a LONG) corresponding to the provided MULTILINESTRING at the specified resolution.

### Syntax
RETURN com.neo4jh3.multilineash3( geographyExpr, resolutionExpr ) 

### Arguments
* geographyExpr: A STRING expression representing a MULTILINESTRING geography in WKT format
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
Returns the H3 cell ID (as a LONG) corresponding to the provided point at the specified resolution.

### Error conditions
If geographyExpr is of type STRING and the value is either an invalid WKT or does not represent a point, the function returns -1

If resolutionExpr is smaller than 0 or larger than 15, the function returns -2

### Example
    RETURN com.neo4jh3.multilineash3('MULTILINESTRING((37.2713558667319 -121.91508032705622), (37.353926450852256 -121.86222328902491))',13) AS value
    635714810904422079
    
    RETURN com.neo4jh3.multilineash3('zzz(37.8199 -122.4783)',13) AS value
    -1
    
    RETURN com.neo4jh3.multilineash3('MULTILINESTRING((37.2713558667319 -121.91508032705622), (37.353926450852256 -121.86222328902491))',16) AS value
    -2

## com.neo4jh3.multilineash3String( geographyExpr, resolutionExpr )
Returns the H3 cell ID (as a STRING) corresponding to the provided MULTILINESTRING at the specified resolution.

### Syntax
RETURN com.neo4jh3.multilineash3String( geographyExpr, resolutionExpr ) 

### Arguments
* geographyExpr: A STRING expression representing a MULTILINESTRING geography in WKT format
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
Returns the H3 cell ID (as a LONG) corresponding to the provided MULTILINESTRING at the specified resolution.

### Error conditions
If geographyExpr is of type STRING and the value is either an invalid WKT or does not represent a point, the function returns -1

If resolutionExpr is smaller than 0 or larger than 15, the function returns -2

### Example
    RETURN com.neo4jh3.multilineash3String('MULTILINESTRING((37.2713558667319 -121.91508032705622), (37.353926450852256 -121.86222328902491))',13) AS value
    8d283409a69a6bf
    
    RETURN com.neo4jh3.multilineash3String('zzz(37.8199 -122.4783)',13) AS value
    -1
    
    RETURN com.neo4jh3.multilineash3String('MULTILINESTRING((37.2713558667319 -121.91508032705622), (37.353926450852256 -121.86222328902491))',16) AS value
    -2

## com.neo4jh3.pointash3( geographyExpr, resolutionExpr )
Returns the H3 cell ID (as a LONG) corresponding to the provided point at the specified resolution.

### Syntax
RETURN com.neo4jh3.pointash3( geographyExpr, resolutionExpr ) 

### Arguments
* geographyExpr: A LONG expression representing a point geography in WKT format
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
Returns the H3 cell ID (as a LONG) corresponding to the provided point at the specified resolution.

### Error conditions
If geographyExpr is of type STRING and the value is either an invalid WKT or does not represent a point, the function returns -1

If resolutionExpr is smaller than 0 or larger than 15, the function returns -2

### Example
    RETURN com.neo4jh3.pointash3('POINT(37.8199 -122.4783)',13) AS value
    635714569676958015
    
    RETURN com.neo4jh3.pointash3('zzz(37.8199 -122.4783)',13) AS value
    -1
    
    RETURN com.neo4jh3.maxChildString('POINT(37.8199 -122.4783)',16) AS value
    -2

## com.neo4jh3.pointash3String( geographyExpr, resolutionExpr )
Returns the H3 cell ID (as a STRING) corresponding to the provided point at the specified resolution.

### Syntax
RETURN com.neo4jh3.pointash3String( geographyExpr, resolutionExpr ) 

### Arguments
* geographyExpr: A STRING expression representing a point geography in WKT format
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution of the child H3 cell ID.

### Returns
Returns the H3 cell ID (as a STRING) corresponding to the provided point at the specified resolution.

### Error conditions
If geographyExpr is of type STRING and the value is either an invalid WKT or does not represent a point, the function returns -1

If resolutionExpr is smaller than 0 or larger than 15, the function returns -2

### Example
    RETURN com.neo4jh3.pointash3String('POINT(37.8199 -122.4783)',13) AS value
    8d283087022a93f
    
    RETURN com.neo4jh3.pointash3String('zzz(37.8199 -122.4783)',13) AS value
    -1
    
    RETURN com.neo4jh3. pointash3String('POINT(37.8199 -122.4783)',16) AS value
    -2

## com.neo4jh3.stringToH3( h3CellIdExpr )
Converts the input string, which is expected to be a hexadecimal string representing an H3 cell, to the corresponding LONG representation of the H3 cell.

### Syntax
RETURN com.neo4jh3.stringToH3( h3CellIdExpr ) AS value;

### Arguments
* h3CellIdExpr: A well-formed hexadecimal STRING expression representing a valid H3 cell ID.

### Returns
A value of type LONG. The function converts the hexadecimal STRING to its corresponding LONG representation.

### Error conditions
If h3CellIdExpr is not a valid H3 cell ID, the function returns -1.

### Example
    RETURN com.neo4jh3.stringToH3('85283473fffffff') AS value
    599686042433355775
        
    RETURN com.neo4jh3.stringToH3('notavalidaddress') AS value
    -1
        
# Procedures
## com.neo4jh3.gridDisk( h3CellIdExpr, kExpr )
Returns the H3 cells that are within (grid) distance k of the origin cell. The set of these H3 cells is called the k-ring of the origin cell.

### Syntax
CALL com.neo4jh3.gridDisk( h3CellIdExpr, kExpr ) yield value return value;

### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.
* kExpr: An INTEGER expression representing the grid distance. kExpr must be non-negative.

### Returns
A list of values of the same type as the type of the h3CellIdExpr expression, corresponding to the H3 cell IDs that have the same resolution as the input H3 cell and are within grid distance k of the input H3 cell, where k is the value of the kExpr.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1
If kExpr < 0, the function returns -2

### Example
    CALL com.neo4jh3.gridDisk(599686042433355775,1) yield value return value;
    599686042433355775,599686030622195711,599686044580839423,599686038138388479,599686043507097599,599686015589810175,599686014516068351
         
    CALL com.neo4jh3.gridDisk(1234,1) yield value return value;
    -1
    
    CALL com.neo4jh3.gridDisk(599686042433355775,-1) yield value return value;
    -2
    
## com.neo4jh3.gridDiskString( h3CellIdExpr, kExpr )
Returns the children H3 cells of the input H3 cell at the specified resolution.

### Syntax
CALL com.neo4jh3.gridDiskString( h3CellIdExpr, kExpr ) yield value return value;

### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.
* kExpr: An INTEGER expression representing the grid distance. kExpr must be non-negative.

### Returns
A list of values of the same type as the type of the h3CellIdExpr expression, corresponding to the H3 cell IDs that have the same resolution as the input H3 cell and are within grid distance k of the input H3 cell, where k is the value of the kExpr.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1
If kExpr < 0, the function returns -2


### Example
    CALL com.neo4jh3.gridDiskString('85283473fffffff',1) yield value return value;
    85283473fffffff,85283447fffffff,8528347bfffffff,85283463fffffff,85283477fffffff,8528340ffffffff,8528340bfffffff
         
    CALL com.neo4jh3.gridDiskString('1234',1) yield value return value;
    -1
    
    CALL com.neo4jh3.gridDiskString('85283473fffffff',-1) yield value return value;
    -2

## com.neo4jh3.polygonToCells( ListOuterGeography, ListHoleGeography, resolutionExpr, LatLonOrder )
Returns a list of H3 cell IDs (represented as LONGs) corresponding to hexagons or pentagons, of the specified resolution, that are contained by the input area geography.

### Syntax
call com.neo4jh3.polygonToCells( ListOuterGeography, ListHoleGeography, resolutionExpr, LatLonOrder ) yield value return value;

### Arguments
* ListOuterGeography: A LIST of latitude and longitude values that express a polygon
* ListHoleGeography: A LIST of latitude and longitude values that express a hole within the ListLatLon polygon
* h3_resolution(h3CellIdExpr) and 15 inclusive, specifying the resolution of the children H3 cell IDs.
* LatLonOrder A STRING that indicates the order of the geometry (latlon or lonlat)

### Returns
A list of H3 cell IDs (represented as LONGs) corresponding to hexagons or pentagons, of the specified resolution, that are contained by the input area geography.

### Error conditions
If resolutionExpr is invalid, the function returns -2

### Example
    call com.neo4jh3.polygonToCells(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value;    
    608692971759468543, 608692970719281151, 608692970736058367, 608692970752835583, 608692975819554815, 608692970769612799, 608692975836332031, 608692970786390015, 608692975853109247, 608692970803167231, 608692975869886463, 608692970819944447, 608692975886663679, 608692975903440895, 608692970585063423, 608692975920218111, 608692970601840639, 608692975685337087, 608692975702114303, 608692975450456063, 608692975718891519, 608692971994349567, 608692970668949503, 608692975467233279, 608692975735668735, 608692972011126783
         
    call com.neo4jh3.polygonToCells(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],20,'latlon') yield value return value
    -2
    
## com.neo4jh3.polygonToCellsString( h3CellIdExpr, resolutionExpr )
Returns a list of H3 cell IDs (represented as STRINGs) corresponding to hexagons or pentagons, of the specified resolution, that are contained by the input area geography.

### Syntax
call com.neo4jh3.polygonToCellsString( h3CellIdExpr, resolutionExpr ) yield value return value;

### Arguments
* ListOuterGeography: A LIST of latitude and longitude values that express a polygon
* ListHoleGeography: A LIST of latitude and longitude values that express a hole within the ListLatLon polygon
* h3_resolution(h3CellIdExpr) and 15 inclusive, specifying the resolution of the children H3 cell IDs.
* LatLonOrder A STRING that indicates the order of the geometry (latlon or lonlat)

### Returns
A list of H3 cell IDs (represented as STRINGs) corresponding to hexagons or pentagons, of the specified resolution, that are contained by the input area geography.

### Error conditions
If resolutionExpr is invalid, the function returns -2

### Example
    call com.neo4jh3.polygonToCellsString(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],7,'latlon') yield value return value;    
    "872830866ffffff", "872830828ffffff", "872830829ffffff", "87283082affffff", "872830958ffffff", "87283082bffffff", "872830959ffffff", "87283082cffffff", "87283095affffff", "87283082dffffff", "87283095bffffff", "87283082effffff", "87283095cffffff", "87283095dffffff", "872830820ffffff", "87283095effffff", "872830821ffffff", "872830950ffffff", "872830951ffffff", "872830942ffffff", "872830952ffffff", "872830874ffffff", "872830825ffffff", "872830943ffffff", "872830953ffffff", "872830875ffffff"
         
    call com.neo4jh3.polygonToCellsString(['37.7866,-122.3805','37.7198,-122.3544','37.7076,-122.5123','37.7835,-122.5247','37.8151,-122.4798'],[],20,'latlon') yield value return value
    -2

## com.neo4jh3.compact( h3CellIdsExpr )
Compacts the input set of H3 cells. The compacted set covers the same set of H3 cells as the original one.

### Syntax
CALL com.neo4jh3.compact( h3CellIdsExpr );

### Arguments
* h3CellIdsExpr: A LIST of LONG expressions representing H3 cell IDs.

### Returns
An LIST of H3 cell IDs of the same type as the values in the input LIST expression h3CellIdsExpr.

### Error conditions


### Example
    CALL com.neo4jh3.compact([599686042433355775,599686030622195711,599686044580839423,599686038138388479,599686043507097599,599686015589810175,599686014516068351,599686034917163007,599686029548453887,599686032769679359,599686198125920255,599686040285872127,599686041359613951,599686039212130303,599686023106002943,599686027400970239,599686013442326527,599686012368584703,599686018811035647]);
    599686030622195711,599686015589810175,599686014516068351,599686034917163007,599686029548453887,599686032769679359,599686198125920255,599686023106002943,599686027400970239,599686013442326527,599686012368584703,599686018811035647,595182446027210751      
    
## com.neo4jh3.compactString( h3CellIdsExpr )
Compacts the input set of H3 cells. The compacted set covers the same set of H3 cells as the original one.

### Syntax
CALL com.neo4jh3.compactString( h3CellIdsExpr );

### Arguments
* h3CellIdsExpr: A LIST of STRING expressions representing H3 cell IDs.

### Returns
An LIST of H3 cell IDs of the same type as the values in the input LIST expression h3CellIdsExpr.

### Error conditions


### Example
    CALL com.neo4jh3.compactString(['85283473fffffff', '85283447fffffff', '8528347bfffffff', '85283463fffffff', '85283477fffffff', '8528340ffffffff', '8528340bfffffff', '85283457fffffff', '85283443fffffff', '8528344ffffffff', '852836b7fffffff', '8528346bfffffff', '8528346ffffffff', '85283467fffffff', '8528342bfffffff', '8528343bfffffff', '85283407fffffff', '85283403fffffff', '8528341bfffffff']);
    
    "85283447fffffff", "8528340ffffffff", "8528340bfffffff", "85283457fffffff", "85283443fffffff", "8528344ffffffff", "852836b7fffffff", "8528342bfffffff", "8528343bfffffff", "85283407fffffff", "85283403fffffff", "8528341bfffffff", "8428347ffffffff" 
    
## com.neo4jh3.gridpathcell( h3CellId1Expr, h3CellId2Expr )
Returns the line of indexes as LONGs between two H3 indexes (inclusive).

### Syntax
CALL com.neo4jh3.gridpathcell( h3CellId1Expr, h3CellId2Expr ) yield value return value

### Arguments
* h3CellId1Expr: A hexadecimal LONG expression representing an H3 cell ID.
* h3CellId2Expr: A hexadecimal LONG expression representing an H3 cell ID.

### Returns
Returns the line of indexes as LONG values between h3CellId1Expr and h3CellId2Expr

### Error conditions
If h3CellId1Expr or h3CellId2Expr is an invalid h3 address, the function returns -1.

### Example
    CALL com.neo4jh3.gridpathcell(599686030622195711, 599686015589810175) yield value return collect(value);    
    599686030622195711, 599686014516068351, 599686015589810175
    
    CALL com.neo4jh3.gridpathcell(604189641121202175,604189642126508543) yield value return value;
    -1

## com.neo4jh3.gridpathcellString( h3CellId1Expr, h3CellId2Expr )
Returns the line of indexes as STRINGs between two H3 indexes (inclusive).

### Syntax
CALL com.neo4jh3.gridpathcellString( h3CellId1Expr, h3CellId2Expr ) yield value return value


### Arguments
* h3CellId1Expr: A hexadecimal STRING expression representing an H3 cell ID.
* h3CellId2Expr: A hexadecimal STRING expression representing an H3 cell ID.

### Returns
Returns the line of indexes as STRING values between h3CellId1Expr and h3CellId2Expr

### Error conditions
If h3CellId1Expr or h3CellId2Expr is an invalid h3 address, the function returns -1.

### Example
    CALL com.neo4jh3.gridpathcellString('862834707ffffff','86283472fffffff') yield value return value
    "862834707ffffff", "86283472fffffff"
    
    CALL com.neo4jh3.gridpathcellString('as331','86283472fffffff') yield value return value
    -1

## com.neo4jh3.gridpathlatlon( latitude1, longitude1, latitude2, longitude2, resolutionExpr )
Returns the line of indexes as LONGs between a pair of latitude/longitude points converted to Hex Addresses at the specified resolution.

### Syntax
CALL com.neo4jh3.gridpathlatlon( latitude1, longitude1, latitude2, longitude2, resolutionExpr ) yield value return value

### Arguments
* latitude1: A DOUBLE expression representing the latitude (in degrees) of the location whose H3 cell ID we want to compute.
* longitude1: A DOUBLE expression representing the longitude (in degrees) of the location whose H3 cell ID we want to compute.
* latitude2: A DOUBLE expression representing the latitude (in degrees) of the location whose H3 cell ID we want to compute.
* longitude2: A DOUBLE expression representing the longitude (in degrees) of the location whose H3 cell ID we want to compute.
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution for the H3 cell ID.

### Returns
Returns the line of indexes as LONG values between the two points

### Error conditions
If latitude1 or latitude2 are invalid, the procedure returns -3
If longitude1 or longitude2 are invalid, the procedure returns -4
If resolutionExpr is invalid, the procedure returns -2

### Example
    CALL com.neo4jh3.gridpathlatlon(37.2,-119.2,38.2,-119.2,5) yield value return value;
    599711803647197183, 599711799352229887, 599711802573455359, 599709700186963967, 599709695891996671, 599709778570117119, 599709774275149823
    
    CALL com.neo4jh3.gridpathlatlon(37.2,-119.2,38.2,-119.2,17) yield value return value;
    -2
    
    CALL com.neo4jh3.gridpathlatlon(97.2,-119.2,38.2,-119.2,5) yield value return collect(value);
    -3
    
    CALL com.neo4jh3.gridpathlatlon(37.2,-219.2,38.2,-119.2,5) yield value return collect(value);
    -4

## com.neo4jh3.gridpathlatlonString( latitude1, longitude1, latitude2, longitude2, resolutionExpr )
Returns the line of indexes as STRINGs between a pair of latitude/longitude points converted to Hex Addresses at the specified resolution.

### Syntax
CALL com.neo4jh3.gridpathlatlonString( latitude1, longitude1, latitude2, longitude2, resolutionExpr ) yield value return value

### Arguments
* latitude1: A DOUBLE expression representing the latitude (in degrees) of the location whose H3 cell ID we want to compute.
* longitude1: A DOUBLE expression representing the longitude (in degrees) of the location whose H3 cell ID we want to compute.
* latitude2: A DOUBLE expression representing the latitude (in degrees) of the location whose H3 cell ID we want to compute.
* longitude2: A DOUBLE expression representing the longitude (in degrees) of the location whose H3 cell ID we want to compute.
* resolutionExpr: An INT expression, whose value is expected to be between 0 and 15 inclusive, specifying the resolution for the H3 cell ID.

### Returns
Returns the line of indexes as STRING values between the two points

### Error conditions
If latitude1 or latitude2 are invalid, the procedure returns -3
If longitude1 or longitude2 are invalid, the procedure returns -4
If resolutionExpr is invalid, the procedure returns -2

### Example
    CALL com.neo4jh3.gridpathlatlonString(37.2,-119.2,38.2,-119.2,5) yield value return value;
    "8529ab53fffffff", "8529ab43fffffff", "8529ab4ffffffff", "85298cb7fffffff", "85298ca7fffffff", "85298ddbfffffff", "85298dcbfffffff"
    
    CALL com.neo4jh3.gridpathlatlonString(37.2,-119.2,38.2,-119.2,17) yield value return value;
    -2
    
    CALL com.neo4jh3.gridpathlatlonString(97.2,-119.2,38.2,-119.2,5) yield value return collect(value);
    -3
    
    CALL com.neo4jh3.gridpathlatlonString(37.2,-219.2,38.2,-119.2,5) yield value return collect(value);
    -4
    
## com.neo4jh3.uncompact( h3CellIdsExpr, resolutionExpr )
Uncompacts the input set of H3 cells to the specified resolution. The uncompacted set covers the same set of H3 cells as the original one using cells at the specified resolution.

### Syntax
call com.neo4jh3.uncompact(h3CellIdsExpr, resolutionExpr) yield value return value 

### Arguments
* h3CellIdsExpr: A LIST of LONG expressions representing H3 cell IDs.
* resolutionExpr:  An INTEGER expression, whose value is expected to be between the maximum resolution of the input H3 cells and 15 inclusive, specifying the resolution of the H3 cell IDs in the output LIST.

### Returns
A LIST of H3 cell IDs of the same type as the values in the input LIST expression h3CellIdsExpr.

### Error conditions
If resolutionExpr is smaller than the maximum resolution of the H3 cell in the input ARRAY, or larger than 15, the function returns -2

### Example
    call com.neo4jh3.uncompact([599686030622195711,599686015589810175,599686014516068351,599686034917163007,599686029548453887,599686032769679359,599686198125920255,599686023106002943,599686027400970239,599686013442326527,599686012368584703,599686018811035647,595182446027210751], 5) yield value return value
    599686030622195711,599686015589810175,599686014516068351,599686034917163007,599686029548453887,599686032769679359,599686198125920255,599686023106002943,599686027400970239,599686013442326527,599686012368584703,599686018811035647,599686038138388479,599686039212130303,599686040285872127,599686041359613951,599686042433355775,599686043507097599,599686044580839423      
    
    call com.neo4jh3.uncompact([599686030622195711,599686015589810175,599686014516068351,599686034917163007,599686029548453887,599686032769679359,599686198125920255,599686023106002943,599686027400970239,599686013442326527,599686012368584703,599686018811035647,595182446027210751], 5) yield value return value
-2
    
## com.neo4jh3.uncompactString( h3CellIdsExpr, resolutionExpr )
Uncompacts the input set of H3 cells to the specified resolution. The uncompacted set covers the same set of H3 cells as the original one using cells at the specified resolution.

### Syntax
CALL com.neo4jh3.uncompactString(h3CellIdsExpr, resolutionExpr);

### Arguments
* h3CellIdsExpr: A LIST of STRING expressions representing H3 cell IDs.
* * resolutionExpr:  An INTEGER expression, whose value is expected to be between the maximum resolution of the input H3 cells and 15 inclusive, specifying the resolution of the H3 cell IDs in the output LIST.

### Returns
An LIST of H3 cell IDs of the same type as the values in the input LIST expression h3CellIdsExpr.

### Error conditions
If resolutionExpr is smaller than the maximum resolution of the H3 cell in the input ARRAY, or larger than 15, the function returns -2

### Example
    CALL com.neo4jh3.uncompactString(["85283447fffffff", "8528340ffffffff", "8528340bfffffff", "85283457fffffff", "85283443fffffff", "8528344ffffffff", "852836b7fffffff", "8528342bfffffff", "8528343bfffffff", "85283407fffffff", "85283403fffffff", "8528341bfffffff", "8428347ffffffff"],5) yield value return value;
    
    "85283447fffffff", "8528340ffffffff", "8528340bfffffff", "85283457fffffff", "85283443fffffff", "8528344ffffffff", "852836b7fffffff", "8528342bfffffff", "8528343bfffffff", "85283407fffffff", "85283403fffffff", "8528341bfffffff", "85283463fffffff", "85283467fffffff", "8528346bfffffff", "8528346ffffffff", "85283473fffffff", "85283477fffffff", "8528347bfffffff"    
    
 CALL com.neo4jh3.uncompactString(["85283447fffffff", "8528340ffffffff", "8528340bfffffff", "85283457fffffff", "85283443fffffff", "8528344ffffffff", "852836b7fffffff", "8528342bfffffff", "8528343bfffffff", "85283407fffffff", "85283403fffffff", "8528341bfffffff", "8428347ffffffff"],0) yield value return value;
 -2

## com.neo4jh3.tochildren( h3CellIdExpr, resolutionExpr )
Returns an array of the children H3 cells of the input H3 cell at the specified resolution.

### Syntax
CALL com.neo4jh3.tochildren(h3CellIdExpr, resolutionExpr) yield value return value;

### Arguments
* h3CellIdExpr: A LONG expression representing an H3 cell ID.
* resolutionExpr: An INT expression, whose value is expected to be between h3_resolution(h3CellIdExpr) and 15 inclusive, specifying the resolution of the children H3 cell IDs.

### Returns
A list of values of values of the same type as the type of the h3CellIdExpr expression, corresponding to the children H3 cell IDs of the input H3 cell at the specified resolution.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    CALL com.neo4jh3.tochildren(599686042433355775,6) yield value return value;
    
    604189641121202175, 604189641255419903, 604189641389637631, 604189641523855359, 604189641658073087, 604189641792290815, 604189641926508543
         
    CALL com.neo4jh3.tochildren(1234,1) yield value return value;
    -1
    
   CALL com.neo4jh3.tochildren(599686042433355775,-1) yield value return value;
    -2
    
## com.neo4jh3.tochildrenString( h3CellIdExpr, resolutionExpr )
Returns an array of the children H3 cells of the input H3 cell at the specified resolution.

### Syntax
CALL com.neo4jh3.tochildrenString(h3CellIdExpr, resolutionExpr) yield value return value;

### Arguments
* h3CellIdExpr: A STRING expression representing an H3 cell ID.
* resolutionExpr: An INT expression, whose value is expected to be between h3_resolution(h3CellIdExpr) and 15 inclusive, specifying the resolution of the children H3 cell IDs.

### Returns
A list of values of values of the same type as the type of the h3CellIdExpr expression, corresponding to the children H3 cell IDs of the input H3 cell at the specified resolution.

### Error conditions
If h3CellIdExpr is invalid, the function returns -1

### Example
    CALL com.neo4jh3.tochildrenString('85283473fffffff',1) yield value return value;
    862834707ffffff,86283470fffffff,862834717ffffff,86283471fffffff,862834727ffffff,86283472fffffff,862834737ffffff
         
    CALL com.neo4jh3.tochildrenString('1234',1) yield value return value;
    -1
    
    CALL com.neo4jh3. tochildrenString('85283473fffffff',-1) yield value return value;
    -2

## Error Codes
* -1 or "-1" : Invalid H3 Address
* -2 or "-2" : Invalid Resolution
* -3 or "-3" : Invalid Latitude
* -4 or "-4" : Invalid Longitude
