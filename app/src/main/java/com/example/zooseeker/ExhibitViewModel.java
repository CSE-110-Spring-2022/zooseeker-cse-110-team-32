package com.example.zooseeker;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/* This class updates the screen with updated information that's stored in the database
 */
public class ExhibitViewModel extends AndroidViewModel {
    private LiveData<List<Exhibit>> exhibitList;
    private final ExhibitDao exhibitDao;

    /*Constructor that initializes the exhibit DAO
       @param  application = the application using the view model
       @param list = list of exhibits user wants to see
     */
    public ExhibitViewModel(@NonNull Application application, PlanList list){
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitDatabase db = ExhibitDatabase.getSingleton(context,list);
        exhibitDao = db.exhibitDao();

    }

    /* Returns the list of exhibits displayed by view model
       @return list of exhibits
     */
    public LiveData<List<Exhibit>> getExhibitItems(){
        if(exhibitList == null){
            loadUsers();
        }
        return exhibitList;
    }

    /* Loads data from database
     */
    private void loadUsers(){
        exhibitList = exhibitDao.getAllLive();
    }

    /* Deletes exhibit from view model and database
       @param exhibit = exhibit to be deleted
     */
    public void deleteExhibit(Exhibit exhibit){

        exhibitDao.delete(exhibit);
    }


}
