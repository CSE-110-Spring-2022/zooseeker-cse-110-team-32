package com.example.zooseeker;

import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.Attribute;

/* This class stores the information related to each edge of the ZooMap (ie each street in the zoo),
including its name, id, the exhibits it connects, and weight (length)
 */
public class IdentifiedWeightedEdge extends DefaultWeightedEdge {
    private String id = null;

    /*returns the edge's ID
    @return id = the given edge's ID
     */
    public String getId() { return id; }

    /*Sets the ID of the edge
    @param id = the edge's ID
     */
    public void setId(String id) { this.id = id; }

    /*Returns the string format of the edge's information including the start node, ID, and end node
    @return string format of edge's information
     */
    @Override
    public String toString() {
        return "(" + getSource() + " :" + id + ": " + getTarget() + ")";
    }

    /* Collects attribute info about an IdentifiedEdgeWeight and sets the edge's id to the Attribute's
    value if the second element in the given pair is "id"
    @param pair = pair of edge and label describing the attribute
    @param attr = attribute containing the edge id
     */
    public static void attributeConsumer(Pair<IdentifiedWeightedEdge, String> pair, Attribute attr) {
        IdentifiedWeightedEdge edge = pair.getFirst();
        String attrName = pair.getSecond();
        String attrValue = attr.getValue();

        if (attrName.equals("id")) {
            edge.setId(attrValue);
        }
    }
}