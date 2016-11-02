package lk.ac.mrt.routing;

import lk.ac.mrt.network.Message;

import java.util.List;

/**
 * Contains the logic for the routing
 */
public class RoutingUtil {

    public List<Node> getConnectableNodes(RoutingTable table){
        //Logic of selecting two ore more(configurable) nodes for message sending
        //Implement random selection


        return null;
    }

    public void handleLeave(Message leaveMessage, RoutingTable table){
        //remove from routing table if leave message received

    }

    public void handleLeave(Node node, RoutingTable table){
        //remove from routing table if heartbeat(periodical ping) failed.
        table.deleteNode(node);
    }

    public void handleRegister(Node node, RoutingTable table){
        //handle the REGOK message
        table.addNode(node);
    }

    public void handleJoin(Node node, RoutingTable table){
        //handlejoin message
    }

    //handle REGISTER messages here (similar to leave)

}
