package com.example.zooseeker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Exhibit.class}, version = 1)
public abstract class ExhibitDatabase extends RoomDatabase {

    private static ExhibitDatabase singleton = null;

    public abstract ExhibitDao exhibitDao();

    public synchronized static ExhibitDatabase getSingleton(Context context, PlanList list) {
        if(singleton == null) {
            singleton = ExhibitDatabase.makeDatabase(context,list);
        }
        return singleton;
    }

    private static ExhibitDatabase makeDatabase(Context context, PlanList list) {
        return Room.databaseBuilder(context, ExhibitDatabase.class, "todo_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<Exhibit> exhibits = list.getExhibits();
                            getSingleton(context, list).exhibitDao().insertAll(exhibits);
                        });

                    }
                }).build();
    }

    @VisibleForTesting
    public static void injectExhibitDatabase(ExhibitDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}
