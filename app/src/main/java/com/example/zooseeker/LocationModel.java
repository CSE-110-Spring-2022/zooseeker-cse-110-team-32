package com.example.zooseeker;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* This class handles the viewModel that is in charge of storing the user's last known location
 */
public class LocationModel extends AndroidViewModel {
    private final String TAG = "FOOBAR";
    private final MediatorLiveData<Coord> lastKnownCoords;
    private LiveData<Coord> locationProviderSource = null;
    private MutableLiveData<Coord> mockSource = null;

    /* Constructor that initializes the last known coordinates of the user and a mock source
       @param application = application that's collecting location information
     */
    public LocationModel(@NonNull Application application) {
        super(application);
        lastKnownCoords = new MediatorLiveData<>();

        // Create and add the mock source.
        mockSource = new MutableLiveData<>();
        lastKnownCoords.addSource(mockSource, lastKnownCoords::setValue);
    }

    /* Returns the last known coordinates of the user for when they close the app and open it again
       @return last known location of the user, as tracked by the app
     */
    public LiveData<Coord> getLastKnownCoords() {
        return lastKnownCoords;
    }

    /**
     * @param locationManager the location manager to request updates from.
     * @param provider        the provider to use for location updates (usually GPS).
     * @apiNote This method should only be called after location permissions have been obtained.
     * @implNote If a location provider source already exists, it is removed.
     */
    @SuppressLint("MissingPermission")
    public void addLocationProviderSource(LocationManager locationManager, String provider) {
        // If a location provider source is already added, remove it.
        if (locationProviderSource != null) {
            removeLocationProviderSource();
        }

        // Create a new GPS source.
        MutableLiveData<Coord> providerSource = new MutableLiveData<Coord>();
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Coord coord = Coord.fromLocation(location);
                Log.i(TAG, String.format("Model received GPS location update: %s", coord));
                providerSource.postValue(coord);
            }
        };
        // Register for updates.
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);

        locationProviderSource = providerSource;
        lastKnownCoords.addSource(locationProviderSource, lastKnownCoords::setValue);
    }

    /* Removes the source that accesses the user's location
     */
    void removeLocationProviderSource() {
        if (locationProviderSource == null) return;
        lastKnownCoords.removeSource(locationProviderSource);
    }

    /* Creates a mock location for testing purposes
       @param coords = mock coordinates you're testing with
     */
    @VisibleForTesting
    public void mockLocation(Coord coords) {
        System.out.println(coords);
        mockSource.postValue(coords);
    }

    /* Creates a mock route for testing purposes
       @param route = route that's being changed
       @param delay = how long the thread will sleep
       @param unit = unit of time that will be converted to milliseconds
       @return mocked route
     */
    @VisibleForTesting
    public Future<?> mockRoute(List<Coord> route, long delay, TimeUnit unit) {
        return Executors.newSingleThreadExecutor().submit(() -> {
            int i = 1;
            int n = route.size();
            for (Coord coord : route) {
                // Mock the location...
                Log.i(TAG, String.format("Model mocking route (%d / %d): %s", i++, n, coord));
                mockLocation(coord);

                // Sleep for a while...
                try {
                    Thread.sleep(unit.toMillis(delay));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

