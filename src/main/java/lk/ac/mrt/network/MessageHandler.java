package lk.ac.mrt.network;

import lk.ac.mrt.common.NetworkUtil;
import lk.ac.mrt.common.PropertyProvider;

public class MessageHandler {

    private static MessageHandler instance;

	private String localIP;
	private int localPort;
	private boolean initialized;

    public static MessageHandler getInstance(){
        if(instance == null){
            instance = new MessageHandler();
        }
        return instance;
    }

	public MessageHandler(  ) {
		this.localIP = NetworkUtil.getIP();
		this.localPort = Integer.parseInt( PropertyProvider.getProperty("PORT"));
		if(localIP != null && !localIP.isEmpty() && localPort > 0)
		{
			initialized = true;
		}
	}

	/**
     * Send a message through UDP and get the response
     * @param msg
     * @param ip
     * @param port
     * @return
     */
    public Response send(Message msg){
        return null;
    }

    /**
     * Open the port and listen
     * @param port
     */
    public void startListening(int port){

    }

    /**
     * Close the port which is listening initiated by {@link #startListening(int)}
     */
    public void stopListening(){

    }


    public void registerForReceiving(MessageType type, MessageListener listener){

    }

    public void setLocalDetails(Message message){
		if(initialized)
		{
			message.setSourceIP( localIP );
			message.setSourcePort( localPort );
		}else{
			new Exception( "Network IP or port not initialized" ).printStackTrace();
		}
	}

}
