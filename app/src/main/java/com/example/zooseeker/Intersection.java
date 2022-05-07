package com.example.zooseeker;

import java.util.List;

/*This class establishes the location type, Intersection, using the Location abstract class.
  This is how the street intersection's information will be stored
 */
public class Intersection extends Location{
    private String id;
    private ZooData.VertexInfo.Kind kind;
    private String name;
    private List<String> tags;

    /*Constructor for Intersection that sets its name, id, and list of tags/categories
    @param id = ID of intersection
    @param name = name of the intersection
    @param tags = list of associated tags/categories with given intersection
     */
    Intersection(String id, String name, List<String> tags) {
        super(id, name, tags);
        this.kind = ZooData.VertexInfo.Kind.INTERSECTION;
    }

}
