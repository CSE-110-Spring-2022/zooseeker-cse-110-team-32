package com.example.zooseeker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
/*
    Assumption: suppose that when we use sort in PlanList, then entrance and exit gate will
    be added automatically, then we can just store the exhibits of PlanList into the database

    Maybe we should have a private long Uid field for database only?
 */
@Dao
public interface ExhibitDao {
    @Insert
    long insert(Exhibit exhibit);

    @Insert
    List<Long> insertAll(List<Exhibit> planList);

    @Query("SELECT * FROM `exhibits` WHERE `Uid`=:Uid")
    Exhibit get(long Uid);

    @Query("SELECT * FROM `exhibits`")
    List<Exhibit> getAll();

    @Update
    int update(Exhibit exhibit);

    @Delete
    int delete(Exhibit exhibit);
}
