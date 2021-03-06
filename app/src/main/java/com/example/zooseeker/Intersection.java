package com.example.zooseeker;

import java.util.List;

/*This class establishes the location type, Intersection, using the Location abstract class.
  This is how the street intersection's information will be stored
 */
public class Intersection extends Location{

    /*Constructor for Intersection that sets its name, id, and list of tags/categories
    @param id = ID of intersection
    @param name = name of the intersection
    @param tags = list of associated tags/categories with given intersection
    @param lat = latitude of intersection
    @param lng = longitude of intersection
     */
    Intersection(String id, String name, double lat, double lng, List<String> tags) {
        super(id, name, lat, lng);
        this.kind = ZooData.VertexInfo.Kind.INTERSECTION;
    }

}
