package com.example.zooseeker;

import java.util.List;

public class Exhibit extends Location{
    private String id;
    private ZooData.VertexInfo.Kind kind;
    private String name;
    private List<String> tags;

    Exhibit(String id, String name, List<String> tags) {
        super(id, name, tags);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT;
    }

}