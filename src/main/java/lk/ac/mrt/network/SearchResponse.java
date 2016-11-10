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

	public int getNoOfFiles() {
		return noOfFiles;
	}

	public void setNoOfFiles(int noOfFiles) {
		this.noOfFiles = noOfFiles;
	}

	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	@Override
	public String marshall() {
		//length SEROK no_files IP port hops filename1 filename2 ... ...
		String appendAll = appendAll(type.code(), noOfFiles, getSourceIP(), getSourcePort(), hops);
		StringBuilder sb =new StringBuilder(appendAll);
		if(results != null) {
			for (String result : results) {
				sb.append(WHITESPACE);
				sb.append(result.replace(' ','_'));
			}
		}
		return sb.toString();
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
		results = new ArrayList<String>();
		i++;
		for (; i < split.length; i++) {
			results.add(split[i].replace('_',' '));
		}
	}

	public void printResult(){
		System.out.println("Fount results from:" + getSourceIP() +":" + getSourcePort());
		System.out.println(results);
	}
}
