package lk.ac.mrt.routing;

/**
 * Class for Node attributes like IP,PORT
 */
public class Node {

    private int nodeID;
    private String ip;
    private int port;


    public Node(int nodeID, String ip, int port) {
        this.nodeID = nodeID;
        this.ip = ip;
        this.port = port;
    }


}
