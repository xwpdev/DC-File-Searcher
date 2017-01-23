package lk.ac.mrt.comment;

import lk.ac.mrt.network.Entity;

import java.util.List;

/**
 * Created by chamika on 1/19/17.
 */
public class Comment extends Entity{
    private Id parentId;
    private Id id;
    private String body;
    private List<Rank> ranks;

    @Override
    public String marshall() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void unmarshall(String messsageData) {
        throw new RuntimeException("Not implemented");
    }
}
