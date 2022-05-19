package com.example.zooseeker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Exhibit.class}, version = 1)
public abstract class ExhibitDatabase extends RoomDatabase {

    public abstract ExhibitDao exhibitDao();
}
