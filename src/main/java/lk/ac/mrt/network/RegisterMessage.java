package lk.ac.mrt.network;

/**
 * Created by chamika on 11/3/16.
 */
public class RegisterMessage extends Message{

    private String username;

    public RegisterMessage() {
        this.type = MessageType.REGISTER;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
