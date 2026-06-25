package org.example;

import java.util.Objects;

public final class Validate {

    private Validate() {}

    public static String requireNonBlank(String value, String field) {
        Objects.requireNonNull(value, field + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    public static double requireNonNegative(double value, String field) {
        if (value < 0) {
            throw new IllegalArgumentException(field + " must not be negative, was: " + value);
        }
        return value;
    }

    public static <T> T requireNonNull(T value, String field) {
        return Objects.requireNonNull(value, field + " must not be null");
    }
}
