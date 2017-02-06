package lk.ac.mrt.comment;

import lk.ac.mrt.network.Entity;

/**
 * Created by chamika on 1/19/17.
 */
public class Rank extends Entity {
    private Id id;
    private int rank;

    @Override
    public String marshall() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void unmarshall(String messsageData) {
        throw new RuntimeException("Not implemented");
    }

    public void generateId(long timestamp, String source) {
        this.id = new Id(timestamp, source, "R", String.valueOf(timestamp));
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }
}
