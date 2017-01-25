package lk.ac.mrt.comment;

import lk.ac.mrt.network.Entity;

import java.util.List;

/**
 * Created by chamika on 1/19/17.
 */
public class Comment extends Entity {
    private Id parentId;
    private Id id;
    private String body;

    public Id getParentId() {
        return parentId;
    }

    public Id getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public List<Comment> getComments() {
        return comments;
    }

    private List<Rank> ranks;
    private List<Comment> comments;

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
        if (obj instanceof Comment) {
            return id.equals(((Comment) obj).id);
        }
        return false;
    }
}
