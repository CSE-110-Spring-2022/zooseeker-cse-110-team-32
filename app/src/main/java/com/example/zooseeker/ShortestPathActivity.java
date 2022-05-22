package com.example.zooseeker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
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
        PlanList plan = SearchActivity.getPlan();
        NavigatePlannedList navList = new NavigatePlannedList(plan);
        Button next = findViewById(R.id.next_btn);
        if(!navList.endReached()){
            displayTextDirections(navList);
        }

        if(next.isClickable()) {
            next.setOnClickListener(view -> {
                displayTextDirections(navList);
            });
        }
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
        // Listen for location updates
        {
            String provider = LocationManager.GPS_PROVIDER;
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener(){
              @Override
              public void onLocationChanged(@NonNull android.location.Location location){
                  Log.d("Zooseeker", String.format("Location changed: %s", location));
              }
            };
            locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
        }
    }

    /*Displays the directions from user's current location to the next closes exhibit in their list
    @param plan = user's planned exhibits
     */
    public void displayTextDirections(NavigatePlannedList navList){
        TextView textView = findViewById(R.id.path_result);
        TextView nextNextView = findViewById(R.id.next_lbl);
        Location currLoc = navList.getCurrentLocation();
        Location nextLoc = navList.getNextLocation();
        String directions = navList.getDirectionsToNextLocation();
        String animalsList = "";
        if (nextLoc.getKind() == ZooData.VertexInfo.Kind.EXHIBIT_GROUP){
            animalsList = " (" + ((ExhibitGroup) nextLoc).getAnimalNameText() + ")";
        }
        directions = "From: " + currLoc.getName() + "\nTo: " + nextLoc.getName() + animalsList + "\n\n" + directions;
        textView.setText(directions);

        Button next = findViewById(R.id.next_btn);
        Button finish = findViewById(R.id.finish_btn);
        if(navList.endReached()){
            next.setClickable(false);
            next.setVisibility(View.GONE);
            nextNextView.setVisibility(View.GONE);

            Intent intent = new Intent(this, SearchActivity.class);
            finish.setClickable(true);
            finish.setVisibility(View.VISIBLE);
            finish.setOnClickListener(view -> {
                startActivity(intent);
            });
        }
        else{
            nextNextView.setText(navList.getNextNextLocation().getName() +
                    ", " + navList.getPathToNextNextLocation().getWeight());
            navList.advanceLocation();
        }
    }

}