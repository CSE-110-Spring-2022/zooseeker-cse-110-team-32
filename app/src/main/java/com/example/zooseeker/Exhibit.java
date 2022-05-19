package com.example.zooseeker;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

/*This class establishes the location type, Exhibit, using the Location abstract class.
  This is how the different animal exhibit's information will be stored
 */
@Entity(tableName = "exhibits")
public class Exhibit extends Location{
    /*Constructor for Exhibit that tells the name, list of tags, id, and other info about a specific
   exhibit
   @param id = ID name of exhibit
   @param name = the actual name of the exhibit
   @param tags = list of tags/categories associated with the exhibit
    */
    @PrimaryKey
    @NonNull
    public String id;

    @TypeConverters(TagConverter.class)
    public List<String> tags;

    @NonNull
    public String name;
    public ZooData.VertexInfo.Kind kind;

    Exhibit(String id, String name, List<String> tags) {
        super(id, name, tags);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT;
    }

    public List<String> getTags() {
        return this.tags;
    }

}
