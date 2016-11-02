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

    public Node(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }


    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
