package com.example.zooseeker;

public class Intersection extends Location {
    private String id;
    private String kind;
    private String name;
    private String[] tags;

    Intersection() {
        this.id = "";
        this.kind = "Intersection";
        this.name = "";
        this.tags = new String[];

    }
    Intersection(String id, String kind, String name, String[] tags) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
    }

    public String getId() { return this.id; }

    public String getKind() { return this.kind; }

    public String getName() {return this.name; }
}
