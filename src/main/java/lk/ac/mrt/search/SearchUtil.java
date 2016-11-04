package lk.ac.mrt.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamika on 11/3/16.
 */
public class SearchUtil {

    public static List<String> search(String query, FilesList list){
        //Search comparison logic implementation
        List<String> matchedFiles = new ArrayList<String>();
        for (String file : list.getFileNames()) {
            if (file.equalsIgnoreCase(query)) {
                matchedFiles.add(file);
            }
        }
        return matchedFiles;
    }
}
