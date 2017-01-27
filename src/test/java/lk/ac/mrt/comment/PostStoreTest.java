package lk.ac.mrt.comment;

import junit.framework.TestCase;

/**
 * Created by chamika on 1/27/17.
 */
public class PostStoreTest extends TestCase {
    private Posts posts1;
    private Posts posts2;

    public void setUp() throws Exception {
        super.setUp();
        posts1 = new Posts();

        String source1 = "node1";
        String source2 = "node2";
        int timestamp = 0;

        File file1 = new File();
        file1.setFileName("Harry Potter");
        file1.generateId(0, source1);
        posts1.addFile(file1);

        Rank rankf1 = new Rank();
        rankf1.setRank(3);
        rankf1.setSource(source1);
        file1.getRanks().add(rankf1);

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
        rank1.setSource(source1);
        comment1.getRanks().add(rank1);

        Rank rank2 = new Rank();
        rank2.setRank(5);
        rank2.setSource(source2);
        comment2.getRanks().add(rank2);

        // 2nd node post
        timestamp = 0;
        posts2 = new Posts();

        File file2 = new File();
        file2.setFileName("Harry Potter");
        file2.generateId(0, source1);
        posts2.addFile(file2);

        Rank rankf2 = new Rank();
        rankf2.setRank(7);
        rankf2.setSource(source1);
        file2.getRanks().add(rankf2);

        Comment comment3 = new Comment();
        comment3.setBody("First comment");
        comment3.generateId(++timestamp, source1);
        file2.getCommentList().add(comment3);

        Comment comment4 = new Comment();
        comment4.setBody("Reply 2 to first comment");
        comment4.generateId(++timestamp, source1);
        comment3.getComments().add(comment4);

        Comment comment5 = new Comment();
        comment5.setBody("Second comment");
        comment5.generateId(++timestamp, source1);
        file2.getCommentList().add(comment5);

        Rank rank3 = new Rank();
        rank3.setRank(1);
        rank3.setSource(source1);
        comment3.getRanks().add(rank3);

        Rank rank4 = new Rank();
        rank4.setRank(5);
        rank4.setSource(source2);
        comment5.getRanks().add(rank4);

    }

    public void testMerge() throws Exception {
        PostStore.merge(posts1);

        //empty merge
        String marshall = new PostsMessage(PostStore.getPosts()).marshall();
        System.out.println(marshall);
        assertEquals("GOSSIP null 0 {\"fileList\":[{\"id\":{\"timestamp\":0,\"source\":\"node1\",\"type\":\"F\",\"hash\":\"-1462237332\"},\"fileName\":\"Harry Potter\",\"commentList\":[{\"parentId\":null,\"id\":{\"timestamp\":1,\"source\":\"node1\",\"type\":\"C\",\"hash\":\"-833364785\"},\"body\":\"First comment\",\"ranks\":[{\"source\":\"node1\",\"rank\":3}],\"comments\":[{\"parentId\":null,\"id\":{\"timestamp\":2,\"source\":\"node1\",\"type\":\"C\",\"hash\":\"783730336\"},\"body\":\"Reply to first comment\",\"ranks\":[{\"source\":\"node2\",\"rank\":5}],\"comments\":[]}]}],\"ranks\":[{\"source\":\"node1\",\"rank\":3}]}],\"timestamp\":0}"
                , marshall);

        //posts merge
        PostStore.merge(posts2);
        Posts merged = PostStore.getPosts();
        marshall = new PostsMessage(merged).marshall();
        System.out.println(marshall);

    }

    public void testAddComment() throws Exception {
        PostStore.merge(posts1);

        String marshall = new PostsMessage(PostStore.getPosts()).marshall();
        System.out.println(marshall);
        assertEquals("GOSSIP null 0 {\"fileList\":[{\"id\":{\"timestamp\":0,\"source\":\"node1\",\"type\":\"F\",\"hash\":\"-1462237332\",\"uid\":\"-14622373320\"},\"fileName\":\"Harry Potter\",\"commentList\":[{\"parentId\":null,\"id\":{\"timestamp\":1,\"source\":\"node1\",\"type\":\"C\",\"hash\":\"-833364785\",\"uid\":\"-8333647851\"},\"body\":\"First comment\",\"ranks\":[{\"source\":\"node1\",\"rank\":3}],\"comments\":[{\"parentId\":null,\"id\":{\"timestamp\":2,\"source\":\"node1\",\"type\":\"C\",\"hash\":\"783730336\",\"uid\":\"7837303362\"},\"body\":\"Reply to first comment\",\"ranks\":[{\"source\":\"node2\",\"rank\":5}],\"comments\":[]}]}],\"ranks\":[{\"source\":\"node1\",\"rank\":3}]}],\"timestamp\":0}",
                marshall);

        Comment comment1 = new Comment();
        comment1.setBody("New comment");
        comment1.generateId(10, "node10");
//        PostStore.addComment("1387228415", comment1);
        PostStore.addComment("7837303362", comment1);

        System.out.println(new PostsMessage(PostStore.getPosts()).marshall());
    }

}