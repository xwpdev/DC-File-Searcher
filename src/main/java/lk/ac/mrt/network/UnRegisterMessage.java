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

}
