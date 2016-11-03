package lk.ac.mrt.routing;


import lk.ac.mrt.common.NetworkUtil;
import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.*;

/**
 * Responsible for routing the messages between nodes and bootsrap server
 *
 * Created by dinu on 11/2/16.
 */
public class Router {

    MessageHandler messageHandler;
    RoutingTable table;

    Router(){
        messageHandler = MessageHandler.getInstance();
    }

    public void register(String ip, int port){

        //Create register message
        RegisterMessage registerMessage = new RegisterMessage();
        setCommonMessageProperties(registerMessage);
        registerMessage.setDestinationIP(PropertyProvider.getProperty("REG_IP"));
        registerMessage.setDestinationPort(Integer.parseInt(PropertyProvider.getProperty("REG_PORT")));
        registerMessage.setUsername(PropertyProvider.getProperty("USERNAME"));

        //Send register message
        RegisterResponse registerResponse= (RegisterResponse) messageHandler.send(registerMessage);


        int nodes = registerResponse.getNumberOfNodes();

        switch(nodes){
            case 2:
            {
                table.addNode(new Node(registerResponse.getIp2(), registerResponse.getPort2()));
            }
            case 1:
            {
                table.addNode(new Node(registerResponse.getIp1(), registerResponse.getPort1()));
            }
            default:
            {

            }
        }


    }

    public void unregister(String ip, int port){

        //Create unregister message
        UnRegisterMessage unRegisterMessage = new UnRegisterMessage();
        setCommonMessageProperties(unRegisterMessage);
        unRegisterMessage.setUsername(PropertyProvider.getProperty("USERNAME"));

        //Send unregister message
        messageHandler.send(unRegisterMessage);

    }

    public void join(String ip, int port){

        //Create join message
        JoinMessage joinMessage = new JoinMessage();
        setCommonMessageProperties(joinMessage);

        // Send join message
        messageHandler.send(joinMessage);

    }

    private void setCommonMessageProperties(Message message){
       	messageHandler.setLocalDetails( message );
    }



}

