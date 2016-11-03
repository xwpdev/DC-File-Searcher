package lk.ac.mrt.routing;


import java.util.ArrayList;
import java.util.List;

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

    public void clearData(){routingTable.clear();}

    public int getSize(){
        return routingTable.size();
    }

    public Node getNode(int index){
        return routingTable.get(index);
    }


}
