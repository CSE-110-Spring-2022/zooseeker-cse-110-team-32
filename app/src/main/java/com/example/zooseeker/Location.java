package com.example.zooseeker;


public abstract class Location {
    private String id;
    private String kind;
    private String name;
    private String[] tags;

    public Location() {}

    public String getId(){
        return this.id;
    }
    public String getKind(){
        return this.kind;
    }
    public String getName(){
        return name;
    }
}
