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


}
