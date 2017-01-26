package lk.ac.mrt.comment;

import lk.ac.mrt.common.StringUtils;
import lk.ac.mrt.network.Message;
import lk.ac.mrt.network.MessageType;

import java.io.IOException;

/**
 * Created by chamika on 1/25/17.
 */
public class PostsMessage extends Message {

    private Posts posts;

    public PostsMessage() {
        this.type = MessageType.GOSSIP;
    }

    public PostsMessage(Posts posts) {
        this.type = MessageType.GOSSIP;
        this.posts = posts;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    @Override
    public String marshall() {
        String childJson = null;
        try {
            childJson = StringUtils.toJson(posts, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appendWithSeparator(WHITESPACE, type.code(), getSourceIP(), getSourcePort(), childJson);
    }

    @Override
    public void unmarshall(String messsageData) {
        String[] splits = messsageData.split(String.valueOf(WHITESPACE), 4);
        setSourceIP(splits[1]);
        setSourcePort(Integer.parseInt(splits[2]));
        try {
            posts = StringUtils.fromJson(splits[3], Posts.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

