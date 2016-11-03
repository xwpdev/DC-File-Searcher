package lk.ac.mrt.network;

/**
 * This class contains the attributes related to Message
 */
public abstract class Message {

    protected final static char WHITESPACE = ' ';

    protected MessageType type;

    private  String destinationIP;
    private int destinationPort;

    private String sourceIP;
    private int sourcePort;

    public abstract String marshall();

    public abstract void unmarshall(String messsageData);

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

    protected String appendAll(Object... obj){
        if(obj == null || obj.length == 0){
            return "";
        }else{
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < obj.length; i++) {
                if(i != 0) {
                    sb.append(WHITESPACE);
                }
                sb.append( obj[i]);

            }
            return sb.toString();
        }
    }
}
