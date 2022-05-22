package com.example.zooseeker;

import android.content.Context;

import org.jgrapht.GraphPath;

import java.util.Map;

public class LocationTracker {

    public double lat;
    public double lng;
    private PlanList plan;
    private Map<String, ZooData.VertexInfo> zooLocs;


    public LocationTracker(Context context, PlanList plan){
        this.plan = plan;
        this.zooLocs = ZooData.loadVertexInfoJSON(context);
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public void setLng(double lng){
        this.lng = lng;
    }

    public String reroute(GraphPath<String, IdentifiedWeightedEdge> currRoute){
        String closestLoc = null;
        double closestDist = Double.MAX_VALUE;
        for (ZooData.VertexInfo loc: zooLocs.values()){
            double dist = Math.pow(Math.pow(lat-loc.lat, 2)+Math.pow(lng-loc.lng, 2), 0.5);
            if (dist < closestDist){
                closestDist = dist;
                closestLoc = loc.id;
            }
        }
        for (String v: currRoute.getVertexList()){
            ZooData.VertexInfo stop = zooLocs.get(v);
            double dist = Math.pow(Math.pow(lat-stop.lat, 2)+Math.pow(lng-stop.lng, 2), 0.5);
            if (closestLoc.equals(v) || closestDist-dist>-0.00001){
                return null;
            }
        }
        return plan.getZooMap().getTextDirections(closestLoc, currRoute.getEndVertex());
    }
}
