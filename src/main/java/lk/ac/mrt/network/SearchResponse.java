package lk.ac.mrt.network;

import java.util.List;

/**
 * Created by dinu on 11/3/16.
 */
public class SearchResponse extends Response{

	private List<String> results;

    public SearchResponse(){this.type = ResponseType.SEARCH; }

	public List<String> getResults()
	{
		return results;
	}

	public void setResults( List<String> results )
	{
		this.results = results;
	}

	@Override
	public String marshall() {
		return null;
	}

	@Override
	public void unmarshall(String messsageData) {

	}
}
