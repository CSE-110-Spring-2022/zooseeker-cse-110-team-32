package com.example.zooseeker;

import java.util.HashMap;
import java.util.Map;

/*This class organizes information about exhibits that are within an exhibit group, like the Owens
  Aviary, including information about the exhibits, their tags, ID, and locations
 */
public class ExhibitGroup extends Location{
    public Map<String, String> animals;

    /*Constructor for ExhibitGroup that tells the name, list of tags, id, and other info about a specific
     exhibit
     @param id = ID name of exhibit
     @param name = the actual name of the exhibit
     @param tags = list of tags/categories associated with the exhibit
    */
    ExhibitGroup(String id, String name, double lat, double lng) {
        super(id, name, lat, lng);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT_GROUP;
        animals = new HashMap<>();
    }

    /* Adds exhibit to the exhibit group
    @param id = ID of exhibit to be added
    @param name = name of exhibit to be added
     */
    public void addAnimal(String id, String name){
        animals.put(id, name);
    }

    /* Returns the exhibits in this exhibit group
       @return exhibits in this group
     */
    public Map<String, String> getAnimals(){
        return animals;
    }

    /* Returns list of animals in this exhibit group as a string containing a list of exhibit names
       @return string format of list of exhibit names in this group
     */
    public String getAnimalNameText(){
        StringBuilder text = new StringBuilder();
        for (String name: animals.values()){
            text.append(name).append(", ");
        }
        text.delete(text.length()-2, text.length());
        return text.toString();
    }
}
