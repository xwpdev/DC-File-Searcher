package lk.ac.mrt.comment;

import lk.ac.mrt.common.StringUtils;
import lk.ac.mrt.network.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by chamika on 1/19/17.
 */
public class Comment extends Entity implements Viewable {
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

    @Override
    public String generateView() {
        StringBuilder sb = new StringBuilder();
        sb.append(getId().generateView()).append(Viewable.NEW_LINE);
        sb.append("Author    : ").append(getId().getSource()).append(Viewable.NEW_LINE);
        sb.append("Comment   : ").append(body).append(Viewable.NEW_LINE);
        StringUtils.generateRating(sb, ranks);
        sb.append(Viewable.NEW_LINE);
        sb.append("Replies   : ");
        if (getComments().size() == 0) {
            sb.append("-- No replies --").append(Viewable.NEW_LINE);
        } else {
            sb.append(Viewable.NEW_LINE);
            ArrayList<Comment> tempComments = new ArrayList<Comment>(getComments());
            Collections.sort(tempComments, new Comparator<Comment>() {
                @Override
                public int compare(Comment o1, Comment o2) {
                    return (int) (o1.getId().getTimestamp() - o2.getId().getTimestamp());
                }
            });
            // add tab character and generate
            StringUtils.generateCommentListView(sb, tempComments);
        }
        sb.append("-----------------------------").append(Viewable.NEW_LINE);

        return sb.toString();
    }
}
