package com.walmart.connect.model;

public enum Location {
    US("us"), IN_BANGALORE("bangalore"), IN_CHENNAI("chennai");

    Location(String name) {
        this.name = name;
    }

    private final String name;
    public String getName() {
        return name;
    }
}
