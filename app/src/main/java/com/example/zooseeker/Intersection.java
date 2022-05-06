package com.example.zooseeker;

import java.util.List;

public class Intersection extends Location{

    Intersection(String id, String name, List<String> tags) {
        super(id, name, tags);
        this.kind = ZooData.VertexInfo.Kind.INTERSECTION;
    }

}
