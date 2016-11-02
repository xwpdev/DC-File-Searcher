package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class JoinMessage extends Message {

    public JoinMessage() {
        this.type = MessageType.JOIN;
    }
}
