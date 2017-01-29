package lk.ac.mrt.comment;

import lk.ac.mrt.network.Entity;

/**
 * Created by chamika on 1/19/17.
 */
public class Id extends Entity implements Viewable {
    private long timestamp;
    private String source;
    private String type;//F=file, C=Comment
    private String hash;
    private String uid;

    public Id() {
    }

    public Id(long timestamp, String source, String type, String hash) {
        this.timestamp = timestamp;
        this.source = source;
        this.type = type;
        this.hash = hash;
    }

    @Override
    public String marshall() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void unmarshall(String messsageData) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Id) {
            Id newId = (Id) obj;
            return source.equals(newId.source) && type.equals(newId.type) && hash.equals(newId.hash);
        }
        return false;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    public String getHash() {
        return hash;
    }

    public String uid() {
        return Integer.toHexString((int) (Math.abs(Integer.parseInt(hash)) + timestamp));
    }

    public String getUid() {
        return uid();
    }

    public void setUid() {
    }


    @Override
    public String generateView() {
        return "ID        : " + uid();
    }
}
