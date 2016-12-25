package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class LeaveMessage extends Message {

    public LeaveMessage() {
        this.type = MessageType.LEAVE;
    }

    @Override
    public String marshall() {
        return appendAll(type.code(), getSourceIP(), getSourcePort());
    }

    @Override
    public void unmarshall(String messsageData) {
        String[] splits = messsageData.split(String.valueOf(WHITESPACE));
        setSourceIP(splits[1]);
        if(splits.length > 2 ) {
            setSourcePort(Integer.parseInt(splits[2]));
        }
    }
}
