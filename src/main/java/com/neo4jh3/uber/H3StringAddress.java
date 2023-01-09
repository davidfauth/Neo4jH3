package com.neo4jh3.uber;

public class H3StringAddress {
    public final String value;

    private H3StringAddress(String hex) {
        this.value = hex;
    }

    public final static H3StringAddress of(long value) {
        return new H3StringAddress(Long.toHexString(value));
    }

    public final static H3StringAddress of(String value) {
        return new H3StringAddress(value);
    }
}
