package com.walmart.connect.model;

public enum Location {
    US("US"), BANGALORE("BANGALORE"), CHENNAI("CHENNAI");

    Location(String name) {
        this.name = name;
    }

    private final String name;
    public String getName() {
        return name;
    }
}
