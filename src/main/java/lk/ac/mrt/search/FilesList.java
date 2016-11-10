package lk.ac.mrt.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chamika on 11/3/16.
 */
public class FilesList {
    private List<String> fileNames = new ArrayList<String>();


    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public void printFileNames(){
        System.out.println(fileNames);
    }
}
