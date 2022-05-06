package com.example.zooseeker;

import java.util.List;

public class Gate extends Location{

    Gate(String id, String name, List<String> tags) {
        super(id, name, tags);
        super.kind = ZooData.VertexInfo.Kind.GATE;
    }

}
