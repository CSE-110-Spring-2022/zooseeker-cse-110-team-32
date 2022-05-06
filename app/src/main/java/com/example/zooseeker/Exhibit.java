package com.example.zooseeker;

import java.util.List;

public class Exhibit extends Location{
    Exhibit(String id, String name, List<String> tags) {
        super(id, name, tags);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT;
    }

}
