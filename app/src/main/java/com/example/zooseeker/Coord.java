package com.example.zooseeker;

import android.location.Location;

import androidx.annotation.NonNull;

/* Class in charge of managing user's actual location. Uses user's actual
location with Location API, but can insert mock locations for testing purposes
 */
public class Coord {
    public final double lat;
    public final double lng;

    /*Constructor that initializes user's latitude and longitude
    @param lat = user latitude
    @param lng = user longitude
     */
    public Coord(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    /* Creates new coordinates based on given latitude and longitude
    @param lat = user latitude
    @param lng = user longitude
    @return new Coord object using given latitude and longitude
     */
    public static Coord of(double lat, double lng) {
        return new Coord(lat, lng);
    }

    /* Returns coordinates of given Location object (gate/exhibit/etc)
       @param location = Location of importance in zoo (exhibit, gate, intersection, etc)
       @return coordinates of given location
     */
    public static Coord fromLocation(Location location) {
        return Coord.of(location.getLatitude(), location.getLongitude());
    }

    /* Overrides 'equals' method to check if the given Object is a Coord and then creates a new Coord
    and compares this and the new Coord's latitudes and longitudes.
    Return true if o is Coord and it's coordinates are the same
    Return false if object is not Coord
    @param o = Object that;s checked to see if it's a Coord
    @return true if Coord and has same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return Double.compare(coord.lat, lat) == 0 && Double.compare(coord.lng, lng) == 0;
    }

    /* Puts coordinates in String format
    @return string format of coordinates
     */
    @NonNull
    @Override
    public String toString() {
        return String.format("Coord{lat=%s, lng=%s}", lat, lng);
    }
}
