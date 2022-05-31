package com.example.zooseeker;

import android.content.Context;
import android.util.Log;

import org.jgrapht.GraphPath;

import java.util.List;
import java.util.Map;

public class LocationTracker {

    public double lat;
    public double lng;
    private PlanList plan;
    private Map<String, ZooData.VertexInfo> zooLocs;
    public GraphPath<String, IdentifiedWeightedEdge> newRoute;


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

    public void reroute(GraphPath<String, IdentifiedWeightedEdge> currRoute){
        String closestLoc = null;
        double closestDist = Double.MAX_VALUE;
        for (ZooData.VertexInfo loc: zooLocs.values()){
            if (loc.parent_id != null){
                continue;
            }
            double dist = distance(lat, lng, loc.lat, loc.lng);
            if (dist < closestDist){
                closestDist = dist;
                closestLoc = loc.id;
            }
        }
        List<String> vertexList = currRoute.getVertexList();
        for (int i = 0; i < vertexList.size()-1;i++){
            String fromid = vertexList.get(i);
            String toid = vertexList.get(i+1);
            ZooData.VertexInfo from = zooLocs.get(fromid);
            double lat1 = from.lat;
            double lng1 = from.lng;
            ZooData.VertexInfo to = zooLocs.get(toid);
            double lat2 = to.lat;
            double lng2 = to.lng;
            double dist = distToStreet(lng, lat, lng1, lat1, lng2, lat2);
            if (closestLoc.equals(fromid) || closestLoc.equals(toid) || closestDist > dist){
                newRoute = currRoute;
                return;
            }
        }
        newRoute = plan.getZooMap().getShortestPath(closestLoc, currRoute.getEndVertex());
    }

    public GraphPath<String, IdentifiedWeightedEdge> getReroute(GraphPath<String, IdentifiedWeightedEdge> currRoute){
        reroute(currRoute);
        return newRoute;
    }

    public int aheadOfCurrentLoc(int currLocIndex) {
        String closestLoc = null;
        double closestDist = Double.MAX_VALUE;
        for (ZooData.VertexInfo loc: zooLocs.values()){
            if (loc.parent_id != null){
                continue;
            }
            double dist = distance(lat, lng, loc.lat, loc.lng);
            if (dist < closestDist){
                closestDist = dist;
                closestLoc = loc.id;
            }
        }
        Log.i("LT", String.format("closestLoc: %s", closestLoc));
        List<Location> vertexList = plan.getMyList();
        for (int i = currLocIndex + 1; i < vertexList.size() - 1; i++) {
            if (closestLoc.equals(vertexList.get(i).getId())) {
                return i;
            }
        }
        return -1;
    }

    public String rerouteTextDirections(GraphPath<String, IdentifiedWeightedEdge> currRoute){
        reroute(currRoute);
        String from = newRoute.getStartVertex();
        String to = newRoute.getEndVertex();
        return plan.getZooMap().getTextDirections(from, to);
    }

    public double distToStreet(double x, double y, double x1, double y1,  double x2, double y2){
        double startdist = distance(x, y, x1, y1);
        double enddist = distance(x, y, x2, y2);
        double linedist = pointDistToLine(x, y, x1, y1, x2, y2);
        double startang = angle(x1, y1, x, y, x2, y2);
        double endang = angle(x2, y2, x, y, x1, y1);
        if (startang >= Math.PI/2 || endang >= Math.PI/2){
            return Math.min(startdist, enddist);
        }
        return linedist;
    }

    public double angle(double vx, double vy, double p1x, double p1y, double p2x, double p2y){
        double vp1 = distance(vx, vy, p1x, p1y);
        double vp2 = distance(vx, vy, p2x, p2y);
        double p1p2 = distance(p1x, p1y, p2x, p2y);
        double vp1sq = Math.pow(vp1, 2);
        double vp2sq = Math.pow(vp2, 2);
        double p1p2sq = Math.pow(p1p2, 2);
        return Math.acos((vp1sq+vp2sq-p1p2sq)/(2*vp1*vp2));
    }

    public double pointDistToLine(double x, double y, double x1, double y1,  double x2, double y2){
        double linelen = distance(x1, y1, x2, y2);
        double numerator = Math.abs((x2-x1)*(y1-y)-(x1-x)*(y2-y1));
        double dist = numerator/linelen;
        return dist;
    }

    public double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2));
    }
}
