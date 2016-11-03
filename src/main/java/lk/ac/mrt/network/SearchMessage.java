package lk.ac.mrt.network;

/**
 * Created by dinu on 11/3/16.
 */
public class SearchMessage extends Message{

	private String keyword;
	private int hopCount;

    public SearchMessage() {
        this.type = MessageType.SEARCH;
    }

	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword( String keyword )
	{
		this.keyword = keyword;
	}

	public int getHopCount()
	{
		return hopCount;
	}

	public void setHopCount( int hopCount )
	{
		this.hopCount = hopCount;
	}
}
