package com.example.zooseeker;


import java.util.List;

public abstract class Location {
    private String id;
    private ZooData.VertexInfo.Kind kind;
    private String name;
    private List<String> tags;

    public Location(String id, String name, List<String> tags){
        this.id = id;
        this.name = name;
        this.tags = tags;
    }

    public String getId() { return this.id; }

    public ZooData.VertexInfo.Kind getKind() { return this.kind; }

    public String getName() {return this.name; }
}
