package com.example.zooseeker;

public class Gate extends Location{
    private String id;
    private String kind;
    private String name;
    private String[] tags;

    Gate() {
        this.id = "";
        this.kind = "gate";
        this.name = "";
        this.tags = null;
    }
    Gate(String id, String kind, String name, String[] tags) {
        this.id = id;
        this.kind = "gate";
        this.name = name;
        this.tags = tags;
    }

    public String getId() { return this.id; }

    public String getKind() { return this.kind; }

    public String getName() {return this.name; }
}
