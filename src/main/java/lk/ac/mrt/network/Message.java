package lk.ac.mrt.network;

/**
 * This class contains the attributes related to Message
 */
public abstract class Message {

    protected MessageType type;
    private  String ip;
    private int port;

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
