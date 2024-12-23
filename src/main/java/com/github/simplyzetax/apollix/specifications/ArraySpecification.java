package com.github.simplyzetax.apollix.specifications;

public class ArraySpecification {

    private final String[] data;

    public ArraySpecification(String[] data) {
        this.data = data;
    }

    public String serialize() {
        return String.join(",", data);
    }

    public static ArraySpecification deserialize(String data) {
        String[] split = data.split(",");
        return new ArraySpecification(split);
    }
}