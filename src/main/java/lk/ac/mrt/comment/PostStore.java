package lk.ac.mrt.comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamika on 1/24/17.
 */
public class PostStore {

    private static Posts posts;

    public PostStore(){
        posts = new Posts();
    }

    public static Posts getPosts() {
        if(posts == null){
            posts = new Posts();
        }
        return posts;
    }

    public static void merge(Posts remotePosts){
        Posts temp = new Posts(posts);
        for(File remoteFile: remotePosts.getFileList()){
            boolean hasFile = false;
            for (File file : getPosts().getFileList()) {
                if(remoteFile.getId().equals(file.getId())){
                    hasFile = true;
                    mergeRanks(file,remoteFile);
                    mergeComments(file,remoteFile);
                    System.out.println("Merged:FILE:" + file.getFileName());
                }

            }
            if(!hasFile){
                temp.addFile(remoteFile);
            }
        }
        posts = temp;
    }


    private static void mergeComments(File file, File remoteFile) {
        List<Comment> commentList = file.getCommentList();
        List<Comment> temp = new ArrayList<Comment>(commentList);
        List<Comment> remoteCommentList = remoteFile.getCommentList();
        for (Comment remoteComment:remoteCommentList){
            boolean hasComment = false;
            for(Comment comment:commentList){
                if(comment.equals(remoteComment)){
                    hasComment = true;
                    mergeRanks(comment,remoteComment);
                    mergeComments(comment,remoteComment);
                }
            }
            if(!hasComment){
                temp.add(remoteComment);
            }
        }
        file.setCommentList(temp);
    }

    private static void mergeComments(Comment comment, Comment remoteComment) {
        if (comment.getId().equals(remoteComment.getId())) {
            if (comment.getComments().isEmpty()) {
                if (remoteComment.getComments().isEmpty()) {
                    //ignore the remote comment
                } else {
                    comment.setComments(remoteComment.getComments());
                }
            } else {
                if (remoteComment.getComments().isEmpty()) {
                    // ignore the remote comment
                } else {
                    mergeReplyComments(comment, remoteComment);
                }
            }
        }
    }

    private static void mergeReplyComments(Comment comment, Comment remoteComment){
        List<Comment> commentList = comment.getComments();
        List<Comment> temp = new ArrayList<Comment>(commentList);
        List<Comment> remoteCommentList = remoteComment.getComments();
        for(Comment remoteReplyComment:remoteCommentList){
            boolean hasComment = false;
            for(Comment replyComment:remoteCommentList){
                if(replyComment.getId().equals(remoteReplyComment.getId())){
                    hasComment = true;
                    mergeRanks(replyComment,remoteReplyComment);
                    mergeComments(replyComment,remoteReplyComment);
                }
            }
            if(!hasComment){
                temp.add(remoteReplyComment);
            }

        }
        comment.setComments(temp);
    }



    private static void mergeRanks(Comment comment, Comment remoteComment) {
        List<Rank> rankList = comment.getRanks();
        List<Rank> temp = new ArrayList<Rank>(rankList);
        List<Rank> remoteRankList = remoteComment.getRanks();
        mergeRankLists(rankList, temp, remoteRankList);
        comment.setRanks(temp);

    }

    private static void mergeRanks(File file, File remoteFile) {
        List<Rank> rankList = file.getRanks();
        List<Rank> temp = new ArrayList<Rank>(rankList);
        List<Rank> remoteRankList = remoteFile.getRanks();
        mergeRankLists(rankList, temp, remoteRankList);
        file.setRanks(temp);
    }

    private static void mergeRankLists(List<Rank> rankList, List<Rank> temp, List<Rank> remoteRankList) {
        for (Rank remoteRank: remoteRankList){
            boolean hasRank = false;
            for(Rank rank:rankList){
                if (remoteRank.getSource().equals(rank.getSource())) {
                    hasRank = true;
                }
            }
            if(!hasRank){
                temp.add(remoteRank);
            }
        }
    }

    public static long getTimestampForUpdate() {
        long timestamp = getPosts().getTimestamp();
        ++timestamp;
        getPosts().setTimestamp(timestamp);
        return timestamp;
    }

    public static void updateTimestamp(long timestamp) {
        getPosts().setTimestamp(Math.max(timestamp, getPosts().getTimestamp()));
    }

    public static void addComment(String id, Comment comment) {
        //TODO add comment for id
        throw new RuntimeException("Not implemented");
    }

    public static void addRank(String id, Rank rank) {
        //TODO add rank for id
        throw new RuntimeException("Not implemented");
    }

}
