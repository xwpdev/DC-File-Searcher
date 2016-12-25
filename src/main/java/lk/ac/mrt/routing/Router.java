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

    private static Router instance;

    MessageHandler messageHandler;
    RoutingTable table;

    public Router() {
        messageHandler = MessageHandler.getInstance();
		table = new RoutingTable();
		initListener();
    }

    public static Router getInstance()
    {
        if ( instance == null )
        {
            instance = new Router();
        }
        return instance;
    }

    private void initListener() {
		//JOIN message handling
        MessageHandler.getInstance().registerForReceiving(MessageType.JOIN, new MessageListener() {
            @Override
            public Response onMessageReceived(Message message) {
                JoinMessage joinMessage = (JoinMessage) message;
                Node node = new Node(joinMessage.getSourceIP(), joinMessage.getSourcePort());
                table.addNode(node);

                JoinResponse joinResponse = new JoinResponse();
                joinResponse.setValue(0);
                joinResponse.copyReturnData(message);
                return joinResponse;
            }

            @Override
            public Response onResponseReceived(Response response) {
                return null;
            }
        });

		//LEAVE message handling
		MessageHandler.getInstance().registerForReceiving(MessageType.LEAVE, new MessageListener() {
			@Override
			public Response onMessageReceived(Message message) {
				LeaveMessage leaveMessage = (LeaveMessage) message;
				Node node = new Node(leaveMessage.getSourceIP(), leaveMessage.getSourcePort());
				table.deleteNode(node);

				LeaveResponse response = new LeaveResponse();
				response.setValue(0);
                response.copyReturnData(message);
				return response;
			}

            @Override
            public Response onResponseReceived(Response response) {
                return null;
            }
        });

        //LIVE message handling
        MessageHandler.getInstance().registerForReceiving(MessageType.LIVE, new MessageListener() {
            @Override
            public Response onMessageReceived(Message message) {
                HeartbeatMessage heartbeatMessage = (HeartbeatMessage) message;
                HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
                heartbeatResponse.setSourceIP(messageHandler.getLocalIP());
                heartbeatResponse.setSourcePort(messageHandler.getLocalPort());
                heartbeatResponse.copyReturnData(message);
                return heartbeatResponse;
            }

            @Override
            public Response onResponseReceived(Response response) {
                return null;
            }
        });

        //LIVEOK message handling
        MessageHandler.getInstance().registerForReceiving(ResponseType.LIVE, new MessageListener() {
            @Override
            public Response onMessageReceived(Message message) {
                return null;
            }

            @Override
            public Response onResponseReceived(Response response) {
                if(response instanceof HeartbeatResponse) {
                    HeartbeatResponse beatResponse = (HeartbeatResponse) response;
                    Node node = table.getNode(beatResponse.getSourceIP(), beatResponse.getSourcePort());
                    if (node != null) {
                        node.setHeartbeats(node.getHeartbeats() + 1);
                    }
                }
                return response;
            }
        });
    }

    public boolean register() {

        //Create register message
        RegisterMessage registerMessage = new RegisterMessage();
        setCommonMessageProperties(registerMessage);
        registerMessage.setDestinationIP(PropertyProvider.getProperty("REG_IP"));
        registerMessage.setDestinationPort(Integer.parseInt(PropertyProvider.getProperty("REG_PORT")));
        registerMessage.setUsername(PropertyProvider.getProperty("USERNAME"));

        //Send register message

        Response response = messageHandler.send(registerMessage);
        if ( response instanceof ErrorResponse){
            ErrorResponse errorResponse = (ErrorResponse) response;
            System.out.println("Error response received");
            return false;
        }else if (response instanceof  RegisterResponse){
            //Handle response
            RegisterResponse registerResponse = (RegisterResponse) response;
            int nodes = registerResponse.getNumberOfNodes();
            if(nodes == 9999){
                System.out.println("failed, there is some error in the command");
                return false;
            }else if (nodes == 9998){
                System.out.println("failed, already registered to you, unregister firs");
                return false;
            }else if (nodes == 9997){
                System.out.println("failed, registered to another user, try a different IP and port");
                return false;
            }else if(nodes == 9996 ){
                System.out.println("failed, can’t register. BS full.");
                return false;
            }
            System.out.println("Successfully Registered");

            List<Node> randomNodes = (List<Node>) getRandomItems(registerResponse.getNodeList(), 2);
            for (Node node : randomNodes) {
                table.addNode(node);
            }
            return true;
        }else{
            System.out.println("Unhandled Response Type for the Request");
            return false;
        }

    }

    public boolean unregister() {

        //Create unregister message
        UnRegisterMessage unRegisterMessage = new UnRegisterMessage();
        setCommonMessageProperties(unRegisterMessage);
        unRegisterMessage.setUsername(PropertyProvider.getProperty("USERNAME"));
        unRegisterMessage.setDestinationIP(PropertyProvider.getProperty("REG_IP"));
        unRegisterMessage.setDestinationPort(Integer.parseInt(PropertyProvider.getProperty("REG_PORT")));

        //Send unregister message
        Response response = messageHandler.send(unRegisterMessage);
        if (response instanceof UnRegisterResponse) {
            UnRegisterResponse unRegisterResponse = (UnRegisterResponse) response;
            //Handle unregister response
            int value = unRegisterResponse.getValue();
            if (value == 0) {
                System.out.println("Successfully Unregistered");
                flushData();
                return true;
            } else if (value == 9999) {
                System.out.println("Error while unregistering. IP and port may not be in the registry or command is incorrect");
                return false;
            } else {
                System.out.println("Unhandled value");
                return false;
            }
        } else if (response instanceof ErrorResponse) {
            System.out.println("Error response received");
            return false;
        }
        return false;
    }


    public boolean join(Node node) {

        //Create join message
        JoinMessage joinMessage = new JoinMessage();
        setCommonMessageProperties(joinMessage);
        joinMessage.setDestinationIP(node.getIp());
        joinMessage.setDestinationPort(node.getPort());

        // Send join message
        Response response = messageHandler.send(joinMessage);

        if(response instanceof JoinResponse){
            JoinResponse joinResponse = (JoinResponse)  response;

            // Handle join response
            int value = joinResponse.getValue();

            if (value == 0) {
                System.out.println("Successfully Joined");
                table.addNode(new Node(joinMessage.getDestinationIP(), joinMessage.getDestinationPort()));
                return true;
            } else if (value == 9999) {
                System.out.println("error while adding new node to routing table");
                return false;
            } else {
                System.out.println("Unhandled value");
                return false;
            }
        }else if (response instanceof ErrorResponse){
            System.out.println("Error response received");
            return false;
        }
        return false;
    }

    public void leave() {

        for (int i = 0; i < table.getSize(); i++) {
            //Create leave message
            LeaveMessage leaveMessage = new LeaveMessage();
            setCommonMessageProperties(leaveMessage);
            leaveMessage.setDestinationIP(table.getNode(i).getIp());
            leaveMessage.setDestinationPort(table.getNode(i).getPort());


            // Send Leave message
            Response response = messageHandler.send(leaveMessage);

            if(response instanceof LeaveResponse){
                LeaveResponse leaveResponse = (LeaveResponse) response;

                // Handle join response
                int value = leaveResponse.getValue();

                if (value == 0) {
                    System.out.println("Successfully left");
                    break;
                } else if (value == 9999) {
                    System.out.println("Leave Failed");
                    break;
                } else {
                    System.out.println("Unhandled value");
                    break;
                }
            }else if (response instanceof ErrorResponse){
                System.out.println("Error response received from "+leaveMessage.getDestinationIP());
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
        List<Node> nodeList = new ArrayList<Node>();
        int max = table.getSize();
        if (max <= limit) {
            for (int i = 0; i < max; i++) {
                nodeList.add(table.getNode(i));
            }
        } else {
            Random rand = new Random();
            Set<Node> set = new HashSet<Node>();
            while (set.size() < limit) {
                int i = rand.nextInt(table.getSize());
                set.add(table.getNode(i));
            }
            nodeList.addAll(set);
        }
        return nodeList;
    }

    public static List getRandomItems(List list, int limit) {
        List nodeList = new ArrayList();
        int max = list.size();
        if (max <= limit) {
            for (int i = 0; i < max; i++) {
                nodeList.add(list.get(i));
            }
        } else {
            Random rand = new Random();
            Set set = new HashSet();
            while (set.size() < limit) {
                int i = rand.nextInt(list.size());
                set.add(list.get(i));
            }
            nodeList.addAll(set);
        }
        return nodeList;
    }

    public List<Node> getAllNodes() {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < table.getSize(); i++) {
            nodes.add(table.getNode(i));
        }
        return nodes;
    }

    public void clearInactive() {
        if (table != null) {
            List<Node> allNodes = getAllNodes();
            for (Node node : allNodes) {
                if (node.getHeartbeats() == 0) {
                    table.deleteNode(node);
                    System.out.println("Removed node due to inactivity:Node=" + node.toString());
                }else{
                    node.setHeartbeats(0);
                }
            }
        }
    }

    public void printRoutingTable(){
		if(table != null)
		{
			for ( int i = 0; i < table.getSize(); i++ )
			{
				System.out.println( "" + table.getNode( i ).getIp() + "                " + table.getNode( i ).getPort() );
			}
		}else{
			System.out.println("Not initialized yet. Please register and join to the system");
		}
    }

    public int getRoutingTableSize(){
        if(table == null){
            return -1;
        }
        return table.getSize();
    }




}

