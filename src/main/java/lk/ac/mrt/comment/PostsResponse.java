package lk.ac.mrt.comment;

import lk.ac.mrt.network.Message;
import lk.ac.mrt.network.Response;

/**
 * Created by chamika on 1/25/17.
 */
public class PostsResponse extends Response {

    private Posts posts;

    public PostsResponse(Posts posts) {
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
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void unmarshall(String messsageData) {
        throw new RuntimeException("Not implemented");
    }
}

