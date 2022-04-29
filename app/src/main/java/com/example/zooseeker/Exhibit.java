package com.example.zooseeker;
public class Exhibit extends Location{
    private String id;
    private String kind;
    private String name;
    private String[] tags;

    Exhibit() {
        this.id = "";
        this.kind = "exhibit";
        this.name = "";
        this.tags = null;
    }
    Exhibit(String id, String name, String[] tags) {
        this.id = id;
        this.kind = "exhibit";
        this.name = name;
        this.tags = tags;
    }

    public String getId() { return this.id; }

    public String getKind() { return this.kind; }

    public String getName() {return this.name; }
}
