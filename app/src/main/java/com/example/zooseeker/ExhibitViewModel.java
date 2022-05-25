package com.example.zooseeker;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ExhibitViewModel extends AndroidViewModel {
    private LiveData<List<Exhibit>> exhibitList;
    private final ExhibitDao exhibitDao;

    public ExhibitViewModel(@NonNull Application application, PlanList list){
        super(application);
        Context context = getApplication().getApplicationContext();
        ExhibitDatabase db = ExhibitDatabase.getSingleton(context,list);
        exhibitDao = db.exhibitDao();

    }
    public LiveData<List<Exhibit>> getExhibitItems(){
        if(exhibitList == null){
            loadUsers();

        }

        return exhibitList;
    }

    private void loadUsers(){
        exhibitList = exhibitDao.getAllLive();
    }

    public void deleteExhibit(Exhibit exhibit){

        exhibitDao.delete(exhibit);
    }


}
