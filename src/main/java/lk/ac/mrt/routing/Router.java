package lk.ac.mrt.routing;

import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.*;

import java.util.*;


/**
 * Responsible for routing the messages between nodes and bootsrap server
 *
 * Created by dinu on 11/2/16.
 */
public class Router {

    MessageHandler messageHandler;
    RoutingTable table;

    public Router() {
        messageHandler = MessageHandler.getInstance();
    }

    public void initListner() {
        MessageHandler.getInstance().registerForReceiving(MessageType.JOIN, new MessageListener() {
            @Override
            public Response onMessageReceived(Message message) {
                JoinMessage joinMessage = (JoinMessage) message;
                Node node = new Node(joinMessage.getSourceIP(), joinMessage.getSourcePort());
                table.addNode(node);

                JoinResponse joinResponse = new JoinResponse();
                joinResponse.setValue(0);
                return joinResponse;
            }
        });

    }

    public void register() {

        //Create register message
        RegisterMessage registerMessage = new RegisterMessage();
        setCommonMessageProperties(registerMessage);
        registerMessage.setDestinationIP(PropertyProvider.getProperty("REG_IP"));
        registerMessage.setDestinationPort(Integer.parseInt(PropertyProvider.getProperty("REG_PORT")));
        registerMessage.setUsername(PropertyProvider.getProperty("USERNAME"));

        //Send register message
        RegisterResponse registerResponse = (RegisterResponse) messageHandler.send(registerMessage);

        //Handle response
        int nodes = registerResponse.getNumberOfNodes();

        switch (nodes) {
            case 2: {
                table.addNode(new Node(registerResponse.getIp2(), registerResponse.getPort2()));
            }
            case 1: {
                table.addNode(new Node(registerResponse.getIp1(), registerResponse.getPort1()));
            }
            default: {
                //do nothing
            }
        }

        System.out.println("Successfully Registered");

    }

    public void unregister() {

        //Create unregister message
        UnRegisterMessage unRegisterMessage = new UnRegisterMessage();
        setCommonMessageProperties(unRegisterMessage);
        unRegisterMessage.setUsername(PropertyProvider.getProperty("USERNAME"));

        //Send unregister message
        UnRegisterResponse unRegisterResponse = (UnRegisterResponse) messageHandler.send(unRegisterMessage);

        //Handle unregister response
        int value = unRegisterResponse.getValue();
        if (value == 0) {
            System.out.println("Successfully Unregistered");
            flushData();
        } else if (value == 9999) {
            System.out.println("Error while unregistering. IP and port may not be in the registry or command is incorrect");
        } else {
            System.out.println("Unhandled value");
        }


    }

    public void join(Node node) {

        //Create join message
        JoinMessage joinMessage = new JoinMessage();
        setCommonMessageProperties(joinMessage);
        joinMessage.setDestinationIP(node.getIp());
        joinMessage.setDestinationPort(node.getPort());

        // Send join message
        JoinResponse joinResponse = (JoinResponse) messageHandler.send(joinMessage);

        // Handle join response
        int value = joinResponse.getValue();

        if (value == 0) {
            System.out.println("Successfully Joined");
            table.addNode(new Node(joinMessage.getDestinationIP(), joinMessage.getDestinationPort()));
        } else if (value == 9999) {
            System.out.println("error while adding new node to routing table");
        } else {
            System.out.println("Unhandled value");
        }

    }

    public void leave() {

        for (int i = 0; i < table.getSize(); i++) {
            //Create leave message
            LeaveMessage leaveMessage = new LeaveMessage();
            setCommonMessageProperties(leaveMessage);
            leaveMessage.setDestinationIP(table.getNode(i).getIp());
            leaveMessage.setDestinationPort(table.getNode(i).getPort());


            // Send jLeave message
            LeaveResponse leaveResponse = (LeaveResponse) messageHandler.send(leaveMessage);

            // Handle join response
            int value = leaveResponse.getValue();

            if (value == 0) {
                System.out.println("Successfully Joined");
            } else if (value == 9999) {
                System.out.println("Leave Failed");
            } else {
                System.out.println("Unhandled value");
            }
        }


    }

    private void setCommonMessageProperties(Message message) {
        messageHandler.setLocalDetails(message);
    }

    private void flushData() {
        table.clearData();
    }

    public List<Node> getRandomNodes(int limit) {
        List<Node> nodeList = new ArrayList<>();
        int max = table.getSize();
        if (max <= limit) {
            for (int i = 0; i <= max; i++) {
                nodeList.add(table.getNode(i));
            }
        } else {
            Random rand = new Random();
            Set<Node> set = new HashSet<>();
            while (set.size() < limit) {
                int i = rand.nextInt(table.getSize());
                set.add(table.getNode(i));
            }
            nodeList.addAll(set);
        }
        return nodeList;
    }

    public void printRoutingTable(){
        for (int i = 0; i < table.getSize() ; i++) {
            System.out.println(""+table.getNode(i).getIp()+"                "+table.getNode(i).getPort());
        }
    }




}

