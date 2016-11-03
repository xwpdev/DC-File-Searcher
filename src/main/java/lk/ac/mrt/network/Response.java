package lk.ac.mrt.network;

/**
 * Response after a network data request
 */
public abstract class Response extends Entity {

    protected ResponseType type;

    private  String destinationIP;
    private int destinationPort;

    private String sourceIP;
    private int sourcePort;

    public ResponseType getType() {
        return type;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public void setDestinationIP(String destinationIP) {
        this.destinationIP = destinationIP;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }
}
