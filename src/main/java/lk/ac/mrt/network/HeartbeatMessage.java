package lk.ac.mrt.network;

/**
 * Created by chamika on 11/11/16.
 */
public class HeartbeatMessage extends Message {

    public HeartbeatMessage() {
        this.type = MessageType.LIVE;
    }

    @Override
    public String marshall() {
        return appendAll(type.code(), getSourceIP(), getSourcePort());
    }

    @Override
    public void unmarshall(String messsageData) {
        String[] splits = messsageData.split(String.valueOf(WHITESPACE));
        setSourceIP(splits[1]);
        setSourcePort(Integer.parseInt(splits[2]));
    }
}
