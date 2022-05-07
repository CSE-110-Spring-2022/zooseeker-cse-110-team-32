package com.example.zooseeker;

import java.util.List;

/*This class establishes the location type, Gate, using the Location abstract class.
  This is how the entrance/exit gate's information will be stored
 */
public class Gate extends Location{
    private String id;
    private ZooData.VertexInfo.Kind kind;
    private String name;
    private List<String> tags;

    /*Constructor for the Gate class that stores the name, ID, and associated tags of the specific
    gate
    @param id = ID associated with the gate
    @param name = actual name of the gate
    @param tags = list of tags/categories associated with the gate
     */
    Gate(String id, String name, List<String> tags) {
        super(id, name, tags);
        this.kind = ZooData.VertexInfo.Kind.GATE;
    }

}
