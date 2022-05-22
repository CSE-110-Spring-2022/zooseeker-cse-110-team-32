package com.example.zooseeker;

/*This class establishes the location type, Exhibit, using the Location abstract class.
  This is how the different animal exhibit's information will be stored
 */
public class Exhibit extends Location{
    /*Constructor for Exhibit that tells the name, list of tags, id, and other info about a specific
   exhibit
   @param id = ID name of exhibit
   @param name = the actual name of the exhibit
   @param tags = list of tags/categories associated with the exhibit
    */
    Exhibit(String id, String name, double lat, double lng) {
        super(id, name, lat, lng);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT;
    }

}
