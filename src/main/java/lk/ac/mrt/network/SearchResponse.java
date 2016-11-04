package lk.ac.mrt.network;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinu on 11/3/16.
 */
public class SearchResponse extends Response{

	private int noOfFiles;
	private int hops;
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
		//length SEROK no_files IP port hops filename1 filename2 ... ...
		String appendAll = appendAll(noOfFiles, getSourceIP(), getSourcePort(), hops);
		StringBuilder sb =new StringBuilder(appendAll);
		if(results != null) {
			for (String result : results) {
				sb.append(WHITESPACE);
				sb.append(result);
			}
		}
		return appendAll;
	}

	@Override
	public void unmarshall(String messsageData) {
		//SEROK no_files IP port hops filename1 filename2 ... ...
		String[] split = messsageData.split(String.valueOf(WHITESPACE));
		int i = 0;
		noOfFiles = Integer.parseInt(split[++i]);
		setSourceIP(split[++i]);
		setSourcePort(Integer.parseInt(split[++i]));
		hops = Integer.parseInt(split[++i]);
		results = new ArrayList<>();
		i++;
		for (; i < split.length; i++) {
			results.add(split[i]);
		}
	}
}
