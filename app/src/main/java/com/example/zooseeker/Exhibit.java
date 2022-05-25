package com.example.zooseeker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
   @param lat = latitude of exhibit
   @param lng = longitude of exhibit
   @param tags = list of tags/categories associated with the exhibit
    */
    @PrimaryKey(autoGenerate = true)
    public long Uid = 0;

    @TypeConverters(TagConverter.class)
    public List<String> tags;

    Exhibit(String id, String name, double lat, double lng, List<String> tags) {
        super(id, name, lat, lng, tags);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT;
        this.tags = tags;
    }

    public List<String> getTags() {
        return this.tags;
    }

    @Override
    public String toString() {
        return "Exhibit{" +
                "tags=" + tags +
                ", id='" + id + '\'' +
                ", kind=" + kind +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }
}
