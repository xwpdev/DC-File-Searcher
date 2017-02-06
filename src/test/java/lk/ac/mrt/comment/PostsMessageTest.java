package lk.ac.mrt.comment;

import junit.framework.TestCase;

/**
 * Created by chamika on 1/25/17.
 */
public class PostsMessageTest extends TestCase {

    private Posts posts;

    public void setUp() throws Exception {
        super.setUp();

        posts = new Posts();

        String source1 = "node1";
        String source2 = "node2";
        int timestamp = -1;

        File file1 = new File();
        file1.setFileName("Harry Potter");
        file1.generateId(++timestamp, source1);
        posts.addFile(file1);

        Comment comment1 = new Comment();
        comment1.setBody("First comment");
        comment1.generateId(++timestamp, source1);
        file1.getCommentList().add(comment1);

        Comment comment2 = new Comment();
        comment2.setBody("Reply to first comment");
        comment2.generateId(++timestamp, source1);
        comment1.getComments().add(comment2);

        Rank rank1 = new Rank();
        rank1.setRank(3);
        rank1.generateId(++timestamp, source1);
        comment2.getRanks().add(rank1);

        Rank rank2 = new Rank();
        rank2.setRank(5);
        rank2.generateId(++timestamp, source2);
        comment2.getRanks().add(rank2);

    }

    public void testMarshall() throws Exception {
        PostsMessage postsMessage = new PostsMessage(posts);
        postsMessage.setSourceIP("192.168.1.1");
        postsMessage.setSourcePort(8080);
        System.out.println(postsMessage.marshall());
    }

    public void testUnmarshall() throws Exception {
        PostsMessage postsMessage = new PostsMessage(null);
        postsMessage.unmarshall("GOSSIP 192.168.1.1 8080 {\"fileList\":[{\"id\":{\"timestamp\":0,\"source\":\"node1\",\"type\":\"F\",\"hash\":\"-1462237332\"},\"fileName\":\"Harry Potter\",\"commentList\":[{\"parentId\":null,\"id\":{\"timestamp\":1,\"source\":\"node1\",\"type\":\"C\",\"hash\":\"-833364785\"},\"body\":\"First comment\",\"ranks\":[],\"comments\":[{\"parentId\":null,\"id\":{\"timestamp\":2,\"source\":\"node1\",\"type\":\"C\",\"hash\":\"783730336\"},\"body\":\"Reply to first comment\",\"ranks\":[{\"source\":\"node1\",\"rank\":3},{\"source\":\"node2\",\"rank\":5}],\"comments\":[]}]}],\"ranks\":[]}]}");
        assertNotNull(postsMessage.getPosts());
    }

}