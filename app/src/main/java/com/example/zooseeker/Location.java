package com.example.zooseeker;


import java.util.List;

/* This creates the Location class that the Exhibit, Gate, and Intersection classes all inherit(?)
from. It stores the location's information such as name, id, type of location, and tags
 */
public abstract class Location {
    private String id;
    private ZooData.VertexInfo.Kind kind;
    private String name;
    private List<String> tags;

    /*Constructor for location that sets the given information of a Location based on the data that
      is passed in
      @param id = ID of location
      @param name = name of the location
      @param tags = list of associated tags/categories of the given location
     */
    public Location(String id, String name, List<String> tags){
        this.id = id;
        this.name = name;
        this.tags = tags;
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
