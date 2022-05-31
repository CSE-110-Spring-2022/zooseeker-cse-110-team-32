package com.example.zooseeker;

/*This class establishes the location type, Gate, using the Location abstract class.
  This is how the entrance/exit gate's information will be stored
 */
public class Gate extends Location{

    /*Constructor for the Gate class that stores the name, ID, and associated tags of the specific
    gate
    @param id = ID associated with the gate
    @param name = actual name of the gate
    @param lat = latitude of exhibit
    @param lng = longitude of exhibit
     */
    Gate(String id, String name, double lat, double lng) {
        super(id, name, lat, lng);
        super.kind = ZooData.VertexInfo.Kind.GATE;
    }

}
