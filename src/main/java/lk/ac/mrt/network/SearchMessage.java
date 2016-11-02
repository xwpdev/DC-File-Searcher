package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class SearchMessage extends Message{

    public SearchMessage() {
        this.type = MessageType.UNREGISTER;
    }
}
