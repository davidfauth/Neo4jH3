package com.dfauth.schema;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
    IN_LOCATION,
    IN_TIMEZONE,
    HAS_PRACTICE_AT,
    HAS_SPECIALTY,
    HAS_BILLING_ADDRESS_AT,
    IS_IN_COUNTY,
    HAS_DISTANCE,
    IS_LOCATED_AT,
    MADE_REFERRAL,
    IS_IN_STATE,
    HAS_SECONDARY_PRACTICE_LOCATION
}
