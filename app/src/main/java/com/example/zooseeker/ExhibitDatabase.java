package com.example.zooseeker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;
import java.util.concurrent.Executors;

/* This class creates the database that stores the information the user inputs, such as the list of
   exhibits in the order they plan to visit them and the exhibit they're currently at.
   Database is cleared after the user taps "finish" indicating they're done with their plan
 */
@Database(entities = {Exhibit.class}, version = 1)
public abstract class ExhibitDatabase extends RoomDatabase {

    private static ExhibitDatabase singleton = null;

    public abstract ExhibitDao exhibitDao();

    /* Returns singleton of given context and list of planned exhibits (as PlanList)
       @param context = information about the zoo
       @param list = list of exhibits user wishes to see
       @return singleton of given information
     */
    public synchronized static ExhibitDatabase getSingleton(Context context, PlanList list) {
        if(singleton == null) {
            singleton = ExhibitDatabase.makeDatabase(context,list);
        }
        return singleton;
    }

    /* Creates the database used to store list of user's exhibit and information of the zoo
       @param context = info about the zoo and exhibits
       @param list = list of exhibits user wants to see
       @return database for saving lists of exhibits
     */
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
                }).fallbackToDestructiveMigration().build();
    }

    /* Allows for using a mock database for testing purposes
       @param testDatabase = mock database used to test the app
     */
    @VisibleForTesting
    public static void injectExhibitDatabase(ExhibitDatabase testDatabase) {
        if (singleton != null) {
            singleton.close();
        }
        singleton = testDatabase;
    }
}
