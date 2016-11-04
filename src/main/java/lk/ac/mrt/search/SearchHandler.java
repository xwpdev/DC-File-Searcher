package lk.ac.mrt.search;

import lk.ac.mrt.common.Constants;
import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.Message;
import lk.ac.mrt.network.MessageHandler;
import lk.ac.mrt.network.MessageListener;
import lk.ac.mrt.network.MessageType;
import lk.ac.mrt.network.Response;
import lk.ac.mrt.network.SearchMessage;
import lk.ac.mrt.network.SearchResponse;
import lk.ac.mrt.routing.Node;
import lk.ac.mrt.routing.Router;

import java.util.List;
import java.util.Map;

/**
 * Created by chamika on 11/3/2016.
 */
public class SearchHandler
{
	private static SearchHandler instance;

	private FilesList filesList;
	private Map<String,Message> messageMap;
	private int maxHopCount;

	public static SearchHandler getInstance()
	{
		if ( instance == null )
		{
			instance = new SearchHandler();
		}
		return instance;
	}

	public SearchHandler()
	{
		initFilesList();
		initMessageMap();
		initSearching();
	}


	private void initSearching()
	{
		MessageHandler.getInstance().registerForReceiving( MessageType.SEARCH, new MessageListener()
		{
			@Override
			public Response onMessageReceived( Message message ) {
				SearchMessage searchMessage = (SearchMessage) message;

				//Check for the duplicate message
				String messageHash = creatHash(message.getSourceIP(), message.getSourcePort(), searchMessage.getKeyword());

				if (!checkDupe(messageHash)) {
					messageMap.put(messageHash, message);
					int hopCount = searchMessage.getHopCount();

					if (hopCount > 0) {
						MessageHandler messageHandler = MessageHandler.getInstance();
						searchMessage.setHopCount(--hopCount);
						//Forward message
						List<Node> randomNodeList = new Router().getRandomNodes( Integer.parseInt( PropertyProvider.getProperty( Constants.FORWARD_COUNT ) ) );
						for (Node n : randomNodeList) {
							searchMessage.setDestinationIP(n.getIp());
							searchMessage.setDestinationPort(n.getPort());

							messageHandler.send( searchMessage );
						}

					} else {
						//Discard
					}
				}


				List<String> searchResults = SearchUtil.search(searchMessage.getKeyword(), filesList);
				if (searchResults != null && !searchResults.isEmpty()) {
					SearchResponse response = new SearchResponse();
					response.setResults(searchResults);
					//set destination ip port
					response.setDestinationIP(searchMessage.getSourceIP());
					response.setDestinationPort(searchMessage.getSourcePort());
					MessageHandler.getInstance().setLocalDetails(message);

					return response;
				}

				return null;
			}
		} );
	}

	private String creatHash(String sourceIP, int sourcePort, String keyword) {
		return new StringBuilder(sourceIP).append(sourcePort).append(keyword).toString();
	}

	private boolean checkDupe(String hash) {

		boolean isDupe = messageMap.containsKey(hash) ? true : false;
		return isDupe;
	}

	private void initFilesList()
	{
		//init files list based on config "FILES"
		String fileMethod = PropertyProvider.getProperty( Constants.CONFIG_FILES );
		if ( Constants.CONFIG_FILES_LIST.equals( fileMethod ) )
		{
			// FILES=LIST
			filesList = FileProvider.getConfiguredFiles();
		}
		else if ( fileMethod.startsWith( Constants.CONFIG_FILES_RANDOM ) )
		{
			// FILES=RANDOM,4
			String[] split = fileMethod.split( "," );
			filesList = FileProvider.getRandomFileList( Integer.parseInt( split[1] ) );
		}
	}


	private void initMessageMap() {
		messageMap = new MessageMap().getMessageMap();
	}

	public Response initiateSearch( String keyword )
	{
		SearchMessage message = new SearchMessage();
		MessageHandler messageHandler = MessageHandler.getInstance();
		messageHandler.setLocalDetails( message );
		message.setKeyword( keyword );
		if ( maxHopCount <= 0 )
		{
			maxHopCount = Integer.parseInt( PropertyProvider.getProperty( Constants.CONFIG_MAX_HOP_COUNT ) );
		}
		message.setHopCount( maxHopCount );

		//send search message
		Router.getInstance().getRandomNodes( Integer.parseInt( PropertyProvider.getProperty( Constants.FORWARD_COUNT ) ) );
		return messageHandler.send( message );
	}

}
