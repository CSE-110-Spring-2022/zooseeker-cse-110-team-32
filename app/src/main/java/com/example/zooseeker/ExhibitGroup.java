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
    public Map<String, Exhibit> animals;
    ExhibitGroup(String id, String name, double lat, double lng) {
        super(id, name, lat, lng);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT_GROUP;
        animals = new HashMap<>();
    }

    public void addAnimal(String id, Exhibit loc){
        animals.put(id, loc);
    }

    public Map<String, Exhibit> getAnimals(){
        return animals;
    }

    public String getAnimalNameText(){
        StringBuilder text = new StringBuilder();
        for (Exhibit loc: animals.values()){
            text.append(loc.name).append(", ");
        }
        text.delete(text.length()-2, text.length());
        return text.toString();
    }
}
