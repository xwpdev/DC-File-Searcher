package lk.ac.mrt.comment;

import lk.ac.mrt.network.Entity;

/**
 * Created by chamika on 1/19/17.
 */
public class Id extends Entity {
    private long timestamp;
    private String source;
    private String type;//F=file, C=Comment
    private String hash;

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
}
