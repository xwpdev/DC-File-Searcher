package lk.ac.mrt.comment;

import lk.ac.mrt.network.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamika on 1/24/17.
 */
public class File extends Entity {

    private Id id;
    private String fileName;
    private List<Comment> commentList;
    private List<Rank> ranks;

    @Override
    public String marshall() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void unmarshall(String messsageData) {
        throw new RuntimeException("Not implemented");
    }

    public Id getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Comment> getCommentList() {
        if (commentList == null) {
            commentList = new ArrayList<Comment>();
        }
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
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

    public void generateId(long timestamp, String source) {
        this.id = new Id(timestamp, source, "F", String.valueOf(fileName.hashCode()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof File) {
            File other = (File) obj;
            Id otherId = other.getId();
            return getId().getType().equals(otherId.getType()) && getId().getHash().equals(otherId.getHash());
        }
        return false;
    }
}
