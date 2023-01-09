package com.neo4jh3.uber;

public class H3LongAddress {
    public final long value;

    private H3LongAddress(long hex) {
        this.value = hex;
    }

    public final static H3LongAddress of(long value) {
        return new H3LongAddress(value);
    }

    public final static H3LongAddress of(String value) {
        if (value.toLowerCase().startsWith("0x")) {
            return new H3LongAddress(
                    Long.parseLong(value.substring(2), 16));
        }
        return new H3LongAddress(Long.parseLong(value, 16));
    }
}
