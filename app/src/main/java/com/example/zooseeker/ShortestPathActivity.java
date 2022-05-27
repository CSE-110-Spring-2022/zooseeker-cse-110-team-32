package com.example.zooseeker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import org.jgrapht.nio.json.JSONImporter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/*This class loads the pages that display the directions from your current location to the next
exhibit with a next button (back button to be added) that is clicked when the user wants to go to
the next exhibit.
 */
public class ShortestPathActivity extends AppCompatActivity {
    PlanList plan;
    NavigatePlannedList navList;
    LocationTracker locTracker;
    private LocationModel model;
    private boolean useLocationService;
    public static final String EXTRA_USE_LOCATION_SERVICE = "use_location_updated";
    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
                perms.forEach((perm, isGranted) -> {
                    Log.i("Zooseeker", String.format("Permission %s granted: %s", perm, isGranted));
                });
            });


    /*Loads directions page and initializes necessary classes and variables for each component
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortest_path);

        Button back = findViewById(R.id.back_btn);
        if(!back.isClickable()){
            back.setVisibility(View.GONE);
        }
        this.plan = SearchActivity.getPlan();
        this.navList = new NavigatePlannedList(plan);
        Button next = findViewById(R.id.next_btn);
        if(!navList.endReached()){
            displayTextDirections();
        }

        if(next.isClickable()) {
            next.setOnClickListener(view -> {
                navList.advanceLocation();
                displayTextDirections();
                buttonVisibility();
            });
        }

        // Set up the model.
        model = new ViewModelProvider(this).get(LocationModel.class);
        useLocationService = getIntent().getBooleanExtra(EXTRA_USE_LOCATION_SERVICE, false);
        // If GPS is enabled, then update the model from the Location service.
        if (useLocationService) {
            // Permissions setup
            {
                String[] requiredPermissions = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };

                Boolean hasNoLocationPerms = Arrays.stream(requiredPermissions)
                        .map(perm -> ContextCompat.checkSelfPermission(this, perm))
                        .allMatch(status -> status == PackageManager.PERMISSION_DENIED);
                if (hasNoLocationPerms){
                    requestPermissionLauncher.launch(requiredPermissions);
                    return;
                }
            }
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String provider = LocationManager.GPS_PROVIDER;
            model.addLocationProviderSource(locationManager, provider);
        }
        // Listen for location updates
//        {
//            this.locTracker = new LocationTracker(this, plan);
//            String provider = LocationManager.GPS_PROVIDER;
//            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//            LocationListener locationListener = new LocationListener(){
//              @Override
//              public void onLocationChanged(@NonNull android.location.Location location){
//                  Log.d("Zooseeker", String.format("Location changed: %s", location));
//                  locTracker.setLat(location.getLatitude());
//                  locTracker.setLng(location.getLongitude());
//                  reroute();
//              }
//            };
//            locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
//        }
        this.locTracker = new LocationTracker(this, plan);
        model.getLastKnownCoords().observe(this, (coord) -> {
            Log.i("Zooseeker", String.format("Observing location model update to %s", coord));
            locTracker.setLat(coord.lat);
            locTracker.setLng(coord.lng);
            System.out.println(locTracker.lat);
            reroute();
        });
    }

    /*Displays the directions from user's current location to the next closes exhibit in their list
    @param plan = user's planned exhibits
     */
    public void displayTextDirections(){
        TextView textView = findViewById(R.id.path_result);
        TextView nextNextView = findViewById(R.id.next_lbl);
        Location currLoc = navList.getCurrentLocation();
        Location nextLoc = navList.getNextLocation();
        String directions = navList.getDirectionsToNextLocation();
        directions = "From: " + currLoc.getName() + "\nTo: " + nextLoc.getName() + "\n\n" + directions;

        textView.setText(directions);
        if(navList.endReached()){
            nextNextView.setVisibility(View.GONE);
        }
        else{
            nextNextView.setText(String.format("%s, %s", navList.getNextNextLocation().getName(), navList.getPathToNextNextLocation().getWeight()));
        }
    }

    public void reroute(){
        TextView textView = findViewById(R.id.path_result);
        Location currLoc = navList.getCurrentLocation();
        Location nextLoc = navList.getNextLocation();
        GraphPath<String, IdentifiedWeightedEdge> currPath = plan.getZooMap().getShortestPath(currLoc.getId(), nextLoc.getId());
        String newRoute = locTracker.rerouteTextDirections(currPath);
        if (newRoute != null) {
            String animalsList = "";
            if (nextLoc.getKind() == ZooData.VertexInfo.Kind.EXHIBIT_GROUP) {
                animalsList = " (" + ((ExhibitGroup) nextLoc).getAnimalNameText() + ")";
            }
            String directions = "From: " + currLoc.getName() + "\nTo: " + nextLoc.getName() + animalsList + "\n\n" + newRoute;
            textView.setText(directions);
        }
    }

    public void buttonVisibility(){
        Button next = findViewById(R.id.next_btn);
        TextView nextNextView = findViewById(R.id.next_lbl);
        Button finish = findViewById(R.id.finish_btn);
        if(navList.endReached()){
            next.setClickable(false);
            next.setVisibility(View.GONE);

            Intent intent = new Intent(this, SearchActivity.class);
            finish.setClickable(true);
            finish.setVisibility(View.VISIBLE);
            finish.setOnClickListener(view -> {
                SearchActivity.getPlan().clearList(SearchActivity.getDao());
                //navList.getPlanList().clearList(SearchActivity.getDao());
                SearchActivity.resetPlan();
                startActivity(intent);
            });
        }
        else{
            nextNextView.setVisibility(View.VISIBLE);
            nextNextView.setText(navList.getNextNextLocation().getName() +
                    ", " + navList.getPathToNextNextLocation().getWeight());
        }
        navList.advanceLocation();

        Button back = findViewById(R.id.back_btn);
        if (!navList.atStart()){
            back.setVisibility(View.VISIBLE);
            back.setClickable(true);
            back.setOnClickListener(view -> {
                displayPrevTextDirections();
            });
        }
    }

    /*
    Displays directions from the next exhibit to the last exhibit to backtrack directions when user clicks back button
    @param navList = user's list of planned exhibits
    */
    public void displayPrevTextDirections(){
        TextView textView = findViewById(R.id.path_result);
        TextView nextNextView = findViewById(R.id.next_lbl);
        Location prevLoc = navList.getPrevLocation();
        Location currLoc = navList.getCurrentLocation();
        String directions = navList.getDirectionsToPreviousLocation();
        directions = "*Going Backwards\n\n" + "From: " + currLoc.getName() + "\nTo: "
                + prevLoc.getName() + "\n\n" + directions;
        textView.setText(directions);

        Button back = findViewById(R.id.back_btn);
        if (navList.atFirst()){
            back.setClickable(false);
            back.setVisibility(View.GONE);
        }

        navList.previousLocation();

        Button next = findViewById(R.id.next_btn);
        Button finish = findViewById(R.id.finish_btn);

        finish.setClickable(false);
        finish.setVisibility(View.GONE);

        next.setVisibility(View.VISIBLE);
        next.setClickable(true);
        next.setOnClickListener(view -> {
            displayTextDirections();
        });
        nextNextView.setVisibility(View.INVISIBLE);
//        if (!navList.endReached()){
//            nextNextView.setVisibility(View.VISIBLE);
//            nextNextView.setText(navList.getNextNextLocation().getName() +
//                    ", " + navList.getPathToNextNextLocation().getWeight());
//        }

    }

    @VisibleForTesting
    public void mockLocation(Coord coords) {
        model.mockLocation(coords);
    }

}
