package com.example.zooseeker;


import java.util.List;

/* This creates the Location class that the Exhibit, Gate, and Intersection classes all inherit(?)
from. It stores the location's information such as name, id, type of location, and tags
 */
public abstract class Location {
    String id;
    ZooData.VertexInfo.Kind kind;
    String name;
    double lat;
    double lng;

    /*Constructor for location that sets the given information of a Location based on the data that
      is passed in
      @param id = ID of location
      @param name = name of the location
      @param lat = latitude of location
      @param lng = longitude of location
     */
    public Location(String id, String name, double lat, double lng){
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    /*returns location's ID
    @return location's ID
     */
    public String getId() { return this.id; }

    /*Returns the type of location (ie Gate, Exhibit, Intersection)
    @return type of location
     */
    public ZooData.VertexInfo.Kind getKind() { return this.kind; }

    /*returns name of location
    @return name of location
     */
    public String getName() {return this.name; }
}
