package lk.ac.mrt.routing;


import lk.ac.mrt.network.Message;
import lk.ac.mrt.network.MessageHandler;
import lk.ac.mrt.network.RegisterMessage;

/**
 * Created by dinu on 11/2/16.
 */
public class Router {

    MessageHandler messageHandler;
    RoutingTable table;

    Router(){
        messageHandler = MessageHandler.getInstance();
    }

    public void register(String ip, int port){

    }

    public void unregister(String ip, int port){

    }

    public void join(String ip, int port){

    }



}

