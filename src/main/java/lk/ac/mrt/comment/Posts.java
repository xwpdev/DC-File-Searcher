package lk.ac.mrt.comment;

import lk.ac.mrt.network.Message;

import java.util.List;

/**
 * Created by chamika on 1/19/17.
 */
public class Posts extends Message {

    private List<File> fileList;

    @Override
    public String marshall() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void unmarshall(String messsageData) {
        throw new RuntimeException("Not implemented");
    }
}
