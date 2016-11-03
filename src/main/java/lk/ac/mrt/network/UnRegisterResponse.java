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


    @Override
    public String marshall() {
        return appendAll(type.code(),getValue());
    }

    @Override
    public void unmarshall(String messsageData) {
        String[] splits = messsageData.split(String.valueOf(WHITESPACE));
        setValue(Integer.parseInt(splits[1]));
    }
}
