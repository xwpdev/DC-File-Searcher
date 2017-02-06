package lk.ac.mrt.routing;

/**
 * Class for Node attributes like IP,PORT
 */
public class Node {

    private String nodeID;
    private String ip;
    private int port;
    private int heartbeats;


    public Node(String nodeID, String ip, int port) {
        this.nodeID = nodeID;
        this.ip = ip;
        this.port = port;
        this.heartbeats = 1;
    }

    public Node(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.heartbeats = 1;
    }


    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
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

    public int getHeartbeats() {
        return heartbeats;
    }

    public void setHeartbeats(int heartbeats) {
        this.heartbeats = heartbeats;
    }

    @Override
    public String toString() {
        return "Node{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", heartbeats=" + heartbeats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (port != node.port) return false;
        return ip.equals(node.ip);
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port;
        return result;
    }
}
