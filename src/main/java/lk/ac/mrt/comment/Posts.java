package lk.ac.mrt.comment;

import lk.ac.mrt.network.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamika on 1/19/17.
 */
public class Posts extends Entity {

    private List<File> fileList;

    public Posts(Posts anotherPost){
        if(anotherPost!= null && anotherPost.getFileList()!=null){
            this.fileList = new ArrayList<File>(anotherPost.getFileList());
        }else{
            fileList = new ArrayList<File>();
        }

    }

    public Posts(){
        fileList = new ArrayList<File>();
    }

    @Override
    public String marshall() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void unmarshall(String messsageData) {
        throw new RuntimeException("Not implemented");
    }

    public List<File> getFileList() {
        if (fileList == null) {
            fileList = new ArrayList<File>();
        }
        return fileList;
    }

    public void addFile(File file) {
        getFileList().add(file);
    }

    public void removeFile(File file) {
        getFileList().remove(file);
    }
}
