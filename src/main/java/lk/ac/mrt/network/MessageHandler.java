package lk.ac.mrt.network;


public class MessageHandler {
    //Make this class singleton


    /**
     * Send a message through UDP and get the response
     * @param msg
     * @param ip
     * @param port
     * @return
     */
    public Response send(Message msg, String ip, long port){
        return null;
    }

    /**
     * Open the port and listen
     * @param port
     */
    public void startListening(long port){

    }

    /**
     * Close the port which is listening initiated by {@link #startListening(long)}
     */
    public void stopListening(){

    }


    public void registerForReceiving(MessageType type, MessageListener listener){

    }


}
