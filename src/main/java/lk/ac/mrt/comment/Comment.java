package lk.ac.mrt.comment;

import lk.ac.mrt.network.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamika on 1/19/17.
 */
public class Comment extends Entity {
    private Id parentId;
    private Id id;
    private String body;
    private List<Rank> ranks;
    private List<Comment> comments;

    public Id getParentId() {
        return parentId;
    }

    public void setParentId(Id parentId) {
        this.parentId = parentId;
    }

    public Id getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Rank> getRanks() {
        if (ranks == null) {
            ranks = new ArrayList<Rank>();
        }
        return ranks;
    }

    public void setRanks(List<Rank> ranks) {
        this.ranks = ranks;
    }

    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<Comment>();
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void generateId(long timestamp, String source) {
        this.id = new Id(timestamp, source, "C", String.valueOf(body.hashCode()));
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
        if (obj instanceof Comment) {
            return id.equals(((Comment) obj).id);
        }
        return false;
    }
}
