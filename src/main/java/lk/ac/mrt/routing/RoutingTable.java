package lk.ac.mrt.routing;


import java.util.ArrayList;

/**
 * Stores the routing table data
 */
public class RoutingTable {

    private ArrayList<Node> routingTable;

    public void addNode(Node node){
        routingTable.add(node);
    }

    public void deleteNode(Node node){
        routingTable.remove(node);
    }

}
