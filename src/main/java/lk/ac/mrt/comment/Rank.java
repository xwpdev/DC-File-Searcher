package lk.ac.mrt.comment;

import lk.ac.mrt.network.Entity;

/**
 * Created by chamika on 1/19/17.
 */
public class Rank extends Entity {
    private String source;
    private int rank;

    @Override
    public String marshall() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void unmarshall(String messsageData) {
        throw new RuntimeException("Not implemented");
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
