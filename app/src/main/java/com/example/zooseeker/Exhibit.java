package com.example.zooseeker;
public class Exhibit extends Location{
    private String id;
    private String kind;
    private String name;
    private String[] tags;

    Exhibit() throws IllegalAccessException {
        throw new IllegalAccessException("Include name");
    }
    Exhibit(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
}
