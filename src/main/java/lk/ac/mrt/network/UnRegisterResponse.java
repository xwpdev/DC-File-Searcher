package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class UnRegisterResponse extends Response {

    int value;

    public UnRegisterResponse(){this.type = ResponseType.UNREGISTER;}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}
