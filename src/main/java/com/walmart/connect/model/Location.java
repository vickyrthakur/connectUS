package com.walmart.connect.model;

public enum Location {
    US("us"), BANGALORE("bangalore"), CHENNAI("chennai");

    Location(String name) {
        this.name = name;
    }

    private final String name;
    public String getName() {
        return name;
    }
}
