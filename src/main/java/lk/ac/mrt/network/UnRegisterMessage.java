package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class UnRegisterMessage extends Message {


    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UnRegisterMessage() {
        this.type = MessageType.UNREGISTER;
    }

    @Override
    public String marshall() {
        return appendAll(type.code(),getSourceIP(), getSourcePort(),getUsername());
    }

    @Override
    public void unmarshall(String messsageData) {
        String[] splits = messsageData.split(String.valueOf(WHITESPACE));
        setSourceIP(splits[1]);
        setSourcePort(Integer.parseInt(splits[2]));
        setUsername(splits[3]);
    }
}
