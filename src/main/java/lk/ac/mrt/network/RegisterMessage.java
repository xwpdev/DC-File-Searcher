package lk.ac.mrt.network;

/**
 * Created by chamika on 11/3/16.
 */
public class RegisterMessage extends Message{

    public RegisterMessage() {
        this.type = MessageType.REGISTER;
    }
}
