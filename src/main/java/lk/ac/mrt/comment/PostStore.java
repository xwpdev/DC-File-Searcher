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
        List<File> fileList = getPosts().getFileList();
        List<File> remoteFileList = remotePosts.getFileList();
        List<File> temp = new ArrayList<File>(fileList);
        for(File remoteFile: remoteFileList){
            boolean hasFile = false;
            for (File file : fileList) {
                if (remoteFile.equals(file)) {
                    hasFile = true;
                    mergeRanks(file,remoteFile);
                    mergeComments(file,remoteFile);
                    //System.out.println("Merged:FILE:" + file.getFileName());
                }

            }
            if(!hasFile){
                temp.add(remoteFile);
            }
        }
        getPosts().setFileList(temp);

        updateTimestamp(remotePosts.getTimestamp());
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
            for (Comment replyComment : commentList) {
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
                if (remoteRank.getId().getSource().equals(rank.getId().getSource())) {
                    hasRank = true;
                    if (remoteRank.getId().getTimestamp() > rank.getId().getTimestamp()) {
                        rank.setRank(remoteRank.getRank());
                        rank.setId(remoteRank.getId());
                    }
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

    public static String addComment(String uid, Comment newComment) {
        if (uid == null) {
            return "Invalid ID";
        }
        //add comment for uid
        Posts posts = getPosts();
        for (File file : posts.getFileList()) {
            if (uid.equals(file.getId().uid())) {
                file.getCommentList().add(newComment);
                return "Comment added to " + file.getFileName();
            }
            for (Comment comment1 : file.getCommentList()) {
                Comment temp = searchComments(uid, comment1);
                if (temp != null) {
                    temp.getComments().add(newComment);
                    return "Comment added to comment ->" + temp.getId().getSource() + ":" + temp.getBody();
                }
            }

        }

        return "No file or comment found for the given ID :" + uid;
    }

    private static Comment searchComments(String uid, Comment comment) {
        if (uid.equals(comment.getId().uid())) {
            return comment;
        } else {
            for (Comment comment1 : comment.getComments()) {
                Comment temp = searchComments(uid, comment1);
                if (temp != null) {
                    return temp;
                }
            }
        }
        return null;
    }

    public static String addRank(String uid, Rank rank) {
        //add rank for id
        if (uid == null) {
            return "Invalid ID";
        }
        //add comment for uid
        Posts posts = getPosts();
        for (File file : posts.getFileList()) {
            if (uid.equals(file.getId().uid())) {
                file.getRanks().add(rank);
                return "Rating added to " + file.getFileName();
            }
            for (Comment comment1 : file.getCommentList()) {
                Comment temp = searchComments(uid, comment1);
                if (temp != null) {
                    temp.getRanks().add(rank);
                    return "Rating added to comment ->" + temp.getId().getSource() + ":" + temp.getBody();
                }
            }
        }
        return "No file or comment found for the given ID :" + uid;
    }

}
