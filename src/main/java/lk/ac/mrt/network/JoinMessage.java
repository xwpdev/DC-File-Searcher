package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class JoinMessage extends Message {

    public JoinMessage() {
        this.type = MessageType.JOIN;
    }

    @Override
    public String marshall() {
        return appendAll(type.code(), getSourceIP(), getSourcePort());
    }

    @Override
    public void unmarshall(String messsageData) {
        String[] splits = messsageData.split(String.valueOf(WHITESPACE));
        if (splits.length == 2) {
            setSourceIP(splits[0]);
            setSourcePort(Integer.parseInt(splits[1]));
        } else {
            setSourceIP(splits[1]);
            setSourcePort(Integer.parseInt(splits[2]));
        }
    }
}
