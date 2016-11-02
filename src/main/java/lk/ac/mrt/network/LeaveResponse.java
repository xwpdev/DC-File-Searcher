package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class LeaveResponse extends Response {

    int value;

    public LeaveResponse(){ this.type = ResponseType.LEAVE;}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
