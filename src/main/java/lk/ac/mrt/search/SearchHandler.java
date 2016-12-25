package lk.ac.mrt.search;

import lk.ac.mrt.common.Constants;
import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.*;
import lk.ac.mrt.routing.Node;
import lk.ac.mrt.routing.Router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by chamika on 11/3/2016.
 */
public class SearchHandler
{
	private static SearchHandler instance;

	private FilesList filesList;
	private Map<String,Message> messageMap;
	private Map<String,Message> searchMap;
    private SearchStat searchStat;
    private int countReceived;
    private int countForwarded;
    private int countAnswered;


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
				final String messageHash = creatHash(message.getSourceIP(), message.getSourcePort(), searchMessage.getKeyword());

                if(searchStat == null){
                    initSearchStat(messageHash);
                }

                searchStat.addReceiveCount();
                countReceived++;

//				if (!checkDupe(messageHash)) {
//					messageMap.put(messageHash, message);
					int hopCount = searchMessage.getHopCount();

					if (hopCount > 0) {

						searchMessage.setHopCount(--hopCount);
						//Forward message
						List<Node> randomNodeList = Router.getInstance().getRandomNodes( Integer.parseInt( PropertyProvider.getProperty( Constants.FORWARD_COUNT ) ) );
						for (Node node : randomNodeList) {
							//Forward to each
							searchMessage.setDestinationIP(node.getIp());
							searchMessage.setDestinationPort(node.getPort());

							MessageHandler messageHandler = MessageHandler.getInstance();
							if (!(message.getSourceIP().equals(messageHandler.getLocalIP()) && message.getSourcePort() == messageHandler.getLocalPort())) {
								MessageHandler.getInstance().send(searchMessage);
								countForwarded++;
								searchStat.addForwardCount();
							}


						}
					}
//				}

                //avoid from sending same
                if(!checkDupe(messageHash)) {
                    List<String> searchResults = SearchUtil.search(searchMessage.getKeyword(), filesList);
                    if (searchResults != null && !searchResults.isEmpty()) {
                        SearchResponse response = new SearchResponse();
                        response.setResults(searchResults);
                        //set destination ip port
                        response.setDestinationIP(searchMessage.getSourceIP());
                        response.setDestinationPort(searchMessage.getSourcePort());
                        response.setHops(((SearchMessage) message).getHopCount());
                        response.setNoOfFiles(searchResults.size());
                        MessageHandler.getInstance().setLocalDetails(response);
                        messageMap.put(messageHash, message);

                        //cache remove after 10 seconds
                        // if need response, we have to start receive and later send the message
                        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                        executor.schedule(new Runnable() {
                            @Override
                            public void run() {
                                messageMap.remove(messageHash);
                            }
                        }, 10, TimeUnit.SECONDS);

						countAnswered++;

                        return response;
                    }
                }

                if(!searchMap.containsKey(messageHash)){
					searchMap.put(messageHash, message);
					final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
					executor.schedule(new Runnable() {
						@Override
						public void run() {
							if(searchStat != null && !searchStat.isSearchInit()) {
								searchStat.write();
								searchStat = null;
							}
						}
					}, 10, TimeUnit.SECONDS);
				}

				return null;
			}

			@Override
			public Response onResponseReceived(Response response) {
				return null;
			}
		} );


		//SEROK message handling
		MessageHandler.getInstance().registerForReceiving(ResponseType.SEARCH, new MessageListener() {
			@Override
			public Response onMessageReceived(Message message) {
				return null;
			}

			@Override
			public Response onResponseReceived(Response response) {
				if(response instanceof SearchResponse) {
					SearchResponse searchResponse = (SearchResponse) response;
					searchResponse.printResult();

					if(searchStat != null) {
						searchStat.addResultCount(searchResponse.getHops());
					}
				}
				return response;
			}
		});
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
		searchMap = new HashMap<String, Message>();
	}

	public boolean initiateSearch( String keyword )
	{
		SearchMessage message = new SearchMessage();
		MessageHandler messageHandler = MessageHandler.getInstance();
		messageHandler.setLocalDetails( message );
		message.setKeyword( keyword );
		int	maxHopCount = Integer.parseInt( PropertyProvider.getProperty( Constants.CONFIG_MAX_HOP_COUNT ) );
		message.setHopCount( maxHopCount );

        final String messageHash = creatHash(message.getSourceIP(), message.getSourcePort(), keyword);
        System.out.println("Search started:" + messageHash);
        initSearchStat(messageHash);
		searchStat.setSearchInit(true);

		//send search message
		List<Node> randomNodes = Router.getInstance().getRandomNodes(Integer.parseInt(PropertyProvider.getProperty(Constants.FORWARD_COUNT)));
		for (Node node : randomNodes) {
			message.setDestinationIP(node.getIp());
			message.setDestinationPort(node.getPort());
			messageHandler.send( message );
            searchStat.addForwardCount();
		}

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
				if(searchStat != null) {
					searchStat.write();
					searchStat = null;
				}
            }
        }, 10, TimeUnit.SECONDS);

		return true;
	}

    private void initSearchStat(String messageHash) {
        searchStat = new SearchStat();
        searchStat.setMaxForwardCount(Integer.parseInt(PropertyProvider.getProperty(Constants.FORWARD_COUNT)));
        searchStat.setMaxHopCount(Integer.parseInt( PropertyProvider.getProperty( Constants.CONFIG_MAX_HOP_COUNT ) ));
        searchStat.setForwardCount(0);
        searchStat.setReceiveCount(0);
        searchStat.setResultCount(0);
		searchStat.setFileName(PropertyProvider.getProperty("USERNAME"));
        searchStat.setMessageHash(messageHash);
		searchStat.setSearchInitTime(System.currentTimeMillis());
		searchStat.setRoutingTableSize(Router.getInstance().getRoutingTableSize());
    }

	public void printAndResetCounts(){
		MessageStat messageStat = new MessageStat();
		messageStat.setFileName(PropertyProvider.getProperty("USERNAME")+"_message");
		messageStat.setCountReceived(countReceived);
		messageStat.setCountForwarded(countForwarded);
		messageStat.setCountAnswered(countAnswered);
		messageStat.write();
		countReceived=0;
		countForwarded=0;
		countAnswered=0;
	}
}
