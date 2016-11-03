package lk.ac.mrt.network;

/**
 * Created by chamika on 11/4/16.
 */
public class ErrorResponse extends Response {

    String value;

    public ErrorResponse() {
        this.type = ResponseType.LEAVE;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String marshall() {
        return appendAll(type.code(), getValue());
    }

    @Override
    public void unmarshall(String messsageData) {
        String[] splits = messsageData.split(String.valueOf(WHITESPACE));
        setValue(splits[1]);
    }
}
