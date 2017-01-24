package lk.ac.mrt.network;

/**
 * Created by chamika on 11/2/16.
 */
public enum ResponseType {
    REGISTER("REGOK"), UNREGISTER("UNROK"), JOIN("JOINOK"), LEAVE("LEAVEOK"), SEARCH("SEROK"), ERROR("ERROR"),
    LIVE("LIVEOK"), GOSSIP("GOSSIP");

    private String typeCode;

    ResponseType(String typeCode) {
        this.typeCode = typeCode;
    }

    public String code() {
        return typeCode;
    }
}
