package lk.ac.mrt.routing;

import lk.ac.mrt.network.Message;

import java.util.List;

/**
 * Created by chamika on 11/2/16.
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
    }

    //handle REGISTER messages here (similar to leave)

}
