package lk.ac.mrt.search;

import lk.ac.mrt.common.Constants;
import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.Message;
import lk.ac.mrt.network.MessageHandler;
import lk.ac.mrt.network.MessageListener;
import lk.ac.mrt.network.MessageType;
import lk.ac.mrt.network.Response;
import lk.ac.mrt.network.SearchMessage;

/**
 * Created by chamika on 11/3/2016.
 */
public class SearchHandler
{
	private static SearchHandler instance;

	private FilesList filesList;
	private MessageHandler messageHandler;
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
		initSearching();
	}

	private void initSearching()
	{
		MessageHandler.getInstance().registerForReceiving( MessageType.SEARCH, new MessageListener()
		{
			@Override
			public Response onMessageReceived( Message message )
			{

				return null;
			}
		} );
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

	public void initiateSearch( String keyword )
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
		messageHandler.send( message );
	}

}
