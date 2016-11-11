package lk.ac.mrt.network;

/**
 * Created by chamika on 11/2/16.
 */
public enum MessageType {
    REGISTER("REG"), UNREGISTER("UNREG"), JOIN("JOIN"), LEAVE("LEAVE"), SEARCH("SER"), LIVE("LIVE");

    private String typeCode;

    MessageType(String typeCode) {
        this.typeCode = typeCode;
    }

    public String code() {
        return typeCode;
    }
}
