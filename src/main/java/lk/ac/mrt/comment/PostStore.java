package lk.ac.mrt.comment;

import java.util.List;

/**
 * Created by chamika on 1/24/17.
 */
public class PostStore {

    private static Posts posts;

    public static Posts getPosts() {
        if(posts == null){
            posts = new Posts();
        }
        return posts;
    }

    public static void merge(Posts remotePosts){
        boolean hasFile = false;
        for(File remoteFile: remotePosts.getFileList()){
            for(File file: posts.getFileList()){
                if(remoteFile.getId().equals(file.getId())){
                    hasFile = true;
                    mergeComments(file,remoteFile);
                }

            }
            if(!hasFile){
                posts.addFile(remoteFile);
                remotePosts.removeFile(remoteFile);
            }
        }

    }

    private static void mergeComments(File file, File remoteFile) {
        boolean hasComment = false;
        List<Comment> commentList = file.getCommentList();
        List<Comment> remoteCommentList = remoteFile.getCommentList();
        for (Comment remoteComment:remoteCommentList){
            for(Comment comment:commentList){
                if(comment.equals(remoteComment)){
                    hasComment = true;
                    mergeRanks(comment,remoteComment);
                }
            }
            if(!hasComment){
                commentList.add(remoteComment);
                remoteCommentList.remove(remoteComment);
            }
        }
    }

    private static void mergeRanks(Comment comment, Comment remoteComment) {
        boolean hasRank = false;
        List<Rank> rankList = comment.getRanks();
        List<Rank> remoteRankList = remoteComment.getRanks();
        for (Rank remoteRank: remoteRankList){
            for(Rank rank:rankList){
                if(remoteRank.getStringSource().equals(rank.getStringSource())){
                    hasRank = true;
                }
            }
            if(!hasRank){
                rankList.add(remoteRank);
                remoteRankList.remove(remoteRank);
            }
        }

    }

}
