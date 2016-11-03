package lk.ac.mrt.routing;


import lk.ac.mrt.common.NetworkUtil;
import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.*;

import java.util.List;

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

        //Handle response
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
                //do nothing
            }
        }

    }

    public void unregister(String ip, int port){

        //Create unregister message
        UnRegisterMessage unRegisterMessage = new UnRegisterMessage();
        setCommonMessageProperties(unRegisterMessage);
        unRegisterMessage.setUsername(PropertyProvider.getProperty("USERNAME"));

        //Send unregister message
        UnRegisterResponse unRegisterResponse = (UnRegisterResponse) messageHandler.send(unRegisterMessage);

        //Handle unregister response
        int value = unRegisterResponse.getValue();
        if ( value == 0){
            System.out.println("Successfully Unregistered");
            flushData();
        }else if (value == 9999){
            System.out.println("Error while unregistering. IP and port may not be in the registry or command is incorrect");
        }else{
            System.out.println("Unhandled value");
        }


    }

    public void join(String ip, int port){

        //Create join message
        JoinMessage joinMessage = new JoinMessage();
        setCommonMessageProperties(joinMessage);

        // Send join message
        JoinResponse joinResponse = (JoinResponse) messageHandler.send(joinMessage);

        // Handle join response
        int value = joinResponse.getValue();

        if (value == 0){
            System.out.println("Successfully Joined");
            table.addNode(new Node(joinMessage.getDestinationIP(),joinMessage.getDestinationPort()));
        }else if (value == 9999){
            System.out.println("error while adding new node to routing table");
        }else{
            System.out.println("Unhandled value");
        }

    }

    public void leave(){

        //Create leave message
        LeaveMessage leaveMessage =new LeaveMessage();
        setCommonMessageProperties(leaveMessage);

        // Send jLeave message
        LeaveResponse leaveResponse = (LeaveResponse) messageHandler.send(leaveMessage);

        // Handle join response
        int value = leaveResponse.getValue();

        if (value == 0){
            System.out.println("Successfully Joined");
        }else if (value == 9999){
            System.out.println("Leave Failed");
        }else{
            System.out.println("Unhandled value");
        }
    }

    private void setCommonMessageProperties(Message message){
       	messageHandler.setLocalDetails( message );
    }

    private void flushData()
    {
        table.clearData();
    }


    public static List<Node> getRandomNodes(int i) {
        //TODO return random nodes
        return null;
    }
}

