package com.example.zooseeker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    public String parentId;
    public String parentName;

    Exhibit(String id, String name, double lat, double lng) {
        super(id, name, lat, lng);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT;
        this.parentId = null;
        this.parentName = null;
    }

    @Ignore
    Exhibit(String id, String name, double lat, double lng, String parentId, String parentName) {
        super(id, name, lat, lng);
        this.kind = ZooData.VertexInfo.Kind.EXHIBIT;
        this.parentId = parentId;
        this.parentName = parentName;
    }


    @Override
    public String toString() {
        return "Exhibit{" +
                ", id='" + id + '\'' +
                ", kind=" + kind +
                ", name='" + name + '\'' +
                '}';
    }
}
