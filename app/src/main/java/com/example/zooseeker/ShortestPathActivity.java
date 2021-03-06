package com.example.zooseeker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.jgrapht.*;
import java.util.Arrays;
/*This class loads the pages that display the directions from your current location to the next
exhibit with a next button (back button to be added) that is clicked when the user wants to go to
the next exhibit.
 */
public class ShortestPathActivity extends AppCompatActivity {
    PlanList plan;
    NavigatePlannedList navList;
    LocationTracker locTracker;
    public ExhibitDao exhibitDao;
    private LocationModel model;
    private AlertDialog alert;
    public boolean askedReplan;
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
        int locIndex = loadIndex();

        //Used for testing different user locations
        mockLocationSetup();

        this.plan = SearchActivity.getPlan();
        this.exhibitDao = SearchActivity.getDao();
        this.navList = new NavigatePlannedList(plan);

        //make sure it doesn't go over
        if (locIndex < plan.planSize()-1) {
            navList.currLocationIndex = locIndex;
        }
        Button next = findViewById(R.id.next_btn);
        Button skip = findViewById(R.id.skip_btn);
        Button back = findViewById(R.id.back_btn);
        if(navList.currLocationIndex < plan.planSize()-1){
            displayTextDirections();
            buttonVisibility();
        }

        if(next.isClickable()) {
            next.setOnClickListener(view -> {
                navList.advanceLocation();
                displayTextDirections();
                buttonVisibility();
                saveIndex();
                askedReplan = false;
            });
        }

        if(skip.isClickable()) {
            skip.setOnClickListener(view -> {
                navList.skip();
                displayTextDirections();
                buttonVisibility();
                saveIndex();
                plan.resetDb(exhibitDao);
                askedReplan = false;
            });
        }

        back.setOnClickListener(view -> {
            navList.previousLocation();
            displayTextDirections();
            buttonVisibility();
            saveIndex();
            askedReplan = false;
        });

        SwitchCompat directionsToggle = findViewById(R.id.directions_switch);

        directionsToggle.setOnCheckedChangeListener(new SwitchCompat.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton switchView, boolean isChecked){
                if (isChecked){
                    plan.getZooMap().setDetailedDirectionSetting();
                    displayTextDirections();
                }
                else{
                    plan.getZooMap().setBriefDirectionSetting();
                    displayTextDirections();
                }
            }
        });

        // Set up the model.
        model = new ViewModelProvider(this).get(LocationModel.class);
        useLocationService = getIntent().getBooleanExtra(EXTRA_USE_LOCATION_SERVICE, false);
        // If GPS is enabled, then update the model from the Location service.
        if (useLocationService) {
            // Permissions setup
            if (permissionsSetup()) return;
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String provider = LocationManager.GPS_PROVIDER;
            model.addLocationProviderSource(locationManager, provider);
        }
        this.locTracker = new LocationTracker(this, plan);
        askedReplan = false;
        model.getLastKnownCoords().observe(this, (coord) -> {
            Log.i("Zooseeker", String.format("Observing location model update to %s", coord));
            locTracker.setLat(coord.lat);
            locTracker.setLng(coord.lng);
            checkOffRoute(coord);
            reroute();
        });
    }

    private void mockLocationSetup() {
        Button mockCoordButton = findViewById(R.id.mock);
        EditText lngText = findViewById(R.id.lng);
        EditText latText = findViewById(R.id.lat);
        mockCoordButton.setOnClickListener(view -> {
            double mockLng = Double.parseDouble(lngText.getText().toString());
            double mockLat = Double.parseDouble(latText.getText().toString());
            Coord mockCoord = new Coord(mockLat, mockLng);
            mockLocation(mockCoord);
        });
    }

    /* Method that sets up permissions to use the user's location
     */
    private boolean permissionsSetup() {
        String[] requiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        boolean hasNoLocationPerms = Arrays.stream(requiredPermissions)
                .map(perm -> ContextCompat.checkSelfPermission(this, perm))
                .allMatch(status -> status == PackageManager.PERMISSION_DENIED);
        if (hasNoLocationPerms){
            requestPermissionLauncher.launch(requiredPermissions);
            return true;
        }
        return false;
    }

    /*Displays the directions from user's current location to the next closes exhibit in their list
    @param prefix = Prefix to attach to directions
     */
    public void displayTextDirections(){
        TextView textView = findViewById(R.id.path_result);
        TextView nextNextView = findViewById(R.id.next_lbl);
        Location currLoc = navList.getCurrentLocation();
        Location nextLoc = navList.getDestination();
        String directions = navList.getDirectionsToDestination();
        String prefix = "";
        if (!navList.goingForwards()){
            prefix = "*Going Backwards\n\n";
        }
        directions = prefix + "From: " + currLoc.getName() + "\nTo: " + nextLoc.getName() + "\n\n" + directions;
        textView.setText(directions);
        if(navList.endReached()){
            nextNextView.setVisibility(View.GONE);
        }
        else{
            nextNextView.setVisibility(View.VISIBLE);
            nextNextView.setText(String.format("%s, %s", navList.getNextNextLocation().getName(), navList.getPathToNextNextLocation().getWeight()));
        }
    }

    public void checkOffRoute(Coord coord) {
        LocationTracker laterLoc = new LocationTracker(this, plan);
        laterLoc.setLng(coord.lng);
        laterLoc.setLat(coord.lat);
        if (laterLoc.aheadOfCurrentLoc(navList.currLocationIndex) != -1 && !askedReplan) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            notifyIfOffTrack(alertBuilder, "Replan?", laterLoc.aheadOfCurrentLoc(navList.currLocationIndex));
            askedReplan = true;
        }
    }

    public void replan(int newLocInd){
        navList.replanOffTrack(newLocInd);
        displayTextDirections();
        plan.resetDb(exhibitDao);
    }

    public void reroute(){
        TextView textView = findViewById(R.id.path_result);
        Location currLoc = navList.getCurrentLocation();
        Location nextLoc = navList.getNextLocation();
        GraphPath<String, IdentifiedWeightedEdge> currPath = plan.getZooMap().getShortestPath(currLoc.getId(), nextLoc.getId());
        String newRoute = locTracker.rerouteTextDirections(currPath);
        //creates new directions for traversing between exhibits
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
        Button skip = findViewById(R.id.skip_btn);
        Button finish = findViewById(R.id.finish_btn);
        Button back = findViewById(R.id.back_btn);
        if(navList.endReached() && navList.goingForwards()){
            next.setClickable(false);
            next.setVisibility(View.GONE);
            Intent intent = new Intent(this, SearchActivity.class);
            finish.setClickable(true);
            finish.setVisibility(View.VISIBLE);
            finish.setOnClickListener(view -> {
                saveIndex(0);
                SearchActivity.getPlan().clearList(SearchActivity.getDao());
                //navList.getPlanList().clearList(SearchActivity.getDao());
                SearchActivity.resetPlan();
                startActivity(intent);
            });
        }
        else{
            setUsable(next);
            setUnusable(finish);
        }
        if(navList.atFirst() && !navList.goingForwards()){
            setUnusable(back);
        }
        else{
            setUsable(back);
        }
        if((navList.endReached() && navList.goingForwards()) || (navList.atFirst() && !navList.goingForwards())){
            setUnusable(skip);
        }
        else{
            setUsable(skip);
        }
    }

    /*Sets the button to be invisible and not clickable
    @param button = button to be made unusable
     */
    public void setUnusable(Button button){
        button.setClickable(false);
        button.setVisibility(View.GONE);
    }

    /*Sets button to be visible and clickable
    @param button = button to be made usable
     */
    public void setUsable(Button button){
        button.setClickable(true);
        button.setVisibility(View.VISIBLE);
    }

    /* saves exhibit user is at to database
     */
    protected void saveIndex() {
        int saved = navList.currLocationIndex;
        saveIndex(saved);
    }

    /* Saves specific exhibit in list that user may not necessarily be at to be database
        @param ind = index of exhibit to be saved
     */
    protected void saveIndex(int ind) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int saved = ind;
        editor.putInt("key", saved);
        boolean yes = editor.commit();
        Log.e("Saving", "saved value is " + saved + " " + yes);

    }

    public int loadIndex() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int locIndex = preferences.getInt("key", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Log.e("loading", "load value is " + locIndex);
        return locIndex;
    }

    public void notifyIfOffTrack(AlertDialog.Builder alertBuilder, String message, int newLocInd) {
        alertBuilder
                .setTitle("Off track!")
                .setMessage(message)
                .setPositiveButton("Yes", (dialog, id) -> {
                    replan(newLocInd);
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
        alert = alertDialog;
        System.out.println(alert);
    }


    @VisibleForTesting
    public void mockLocation(Coord coords) {
        model.mockLocation(coords);
    }

    @VisibleForTesting
    public AlertDialog getLastAlertDialog() {return this.alert;}

}
