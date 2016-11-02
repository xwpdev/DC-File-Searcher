package lk.ac.mrt.network;


public class MessageHandler {

    private static MessageHandler instance;

    public static MessageHandler getInstance(){
        if(instance == null){
            instance = new MessageHandler();
        }
        return instance;
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


}
