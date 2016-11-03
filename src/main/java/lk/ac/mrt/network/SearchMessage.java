package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class SearchMessage extends Message {

    private String keyword;
    private int hopCount;

    public SearchMessage() {
        this.type = MessageType.SEARCH;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getHopCount() {
        return hopCount;
    }

    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }

    //SER IP port file_name hops
    @Override
    public String marshall() {
        return appendAll(type.code(), getSourceIP(), getSourcePort(), keyword, hopCount);
    }

    @Override
    public void unmarshall(String messsageData) {
        String[] splits = messsageData.split(String.valueOf(WHITESPACE));
        setSourceIP(splits[1]);
        setSourcePort(Integer.parseInt(splits[2]));
        setKeyword(splits[3]);
        setHopCount(Integer.parseInt(splits[4]));
    }
}
