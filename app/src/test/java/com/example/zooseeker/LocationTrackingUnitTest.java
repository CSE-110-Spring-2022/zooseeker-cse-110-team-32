package com.example.zooseeker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationTrackingUnitTest {
    @Before
    public void createContext(){
        Context context = ApplicationProvider.getApplicationContext();
    }

}
