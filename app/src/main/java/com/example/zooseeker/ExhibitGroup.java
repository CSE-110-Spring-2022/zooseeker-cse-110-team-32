package com.example.zooseeker;

import java.util.HashMap;
import java.util.Map;

public class ExhibitGroup extends Location{
    /*Constructor for ExhibitGroup that tells the name, list of tags, id, and other info about a specific
  exhibit
  @param id = ID name of exhibit
  @param name = the actual name of the exhibit
  @param tags = list of tags/categories associated with the exhibit
   */
    public Map<String, String> animals;
    ExhibitGroup(String id, String name, double lat, double lng) {
        super(id, name, lat, lng);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT_GROUP;
        animals = new HashMap<>();
    }

    public void addAnimal(String id, String name){
        animals.put(id, name);
    }

    public Map<String, String> getAnimals(){
        return animals;
    }
}
