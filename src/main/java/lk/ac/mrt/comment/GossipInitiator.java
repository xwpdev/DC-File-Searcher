package lk.ac.mrt.comment;

import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.*;
import lk.ac.mrt.routing.Node;
import lk.ac.mrt.routing.Router;

import java.util.*;

/**
 * Created by chamika on 1/24/17.
 */
public class GossipInitiator {
    private static GossipInitiator INSTANCE;
    private Timer timer;
    private Map<Node, Long> syncTimestamps = new HashMap<Node, Long>();

    public static GossipInitiator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GossipInitiator();
        }
        return INSTANCE;
    }

    private GossipInitiator() {
        //GOSSIP message handling
        MessageHandler.getInstance().registerForReceiving(MessageType.GOSSIP, new MessageListener() {
            @Override
            public Response onMessageReceived(Message message) {
                Posts posts = ((PostsMessage) message).getPosts();
                PostStore.merge(posts);
                MessageHandler messageHandler = MessageHandler.getInstance();
                PostsResponse postsResponse = new PostsResponse(PostStore.getPosts());
                postsResponse.setSourceIP(messageHandler.getLocalIP());
                postsResponse.setSourcePort(messageHandler.getLocalPort());
                postsResponse.copyReturnData(message);
                return postsResponse;
            }

            @Override
            public Response onResponseReceived(Response response) {
                Posts posts = ((PostsResponse) response).getPosts();
                PostStore.merge(posts);
                return response;
            }
        });

        MessageHandler.getInstance().registerForReceiving(ResponseType.GOSSIP, new MessageListener() {
            @Override
            public Response onMessageReceived(Message message) {
                Posts posts = ((PostsMessage) message).getPosts();
                PostStore.merge(posts);
                MessageHandler messageHandler = MessageHandler.getInstance();
                PostsResponse postsResponse = new PostsResponse(PostStore.getPosts());
                postsResponse.setSourceIP(messageHandler.getLocalIP());
                postsResponse.setSourcePort(messageHandler.getLocalPort());
                postsResponse.copyReturnData(message);
                return postsResponse;
            }

            @Override
            public Response onResponseReceived(Response response) {
                return null;
            }
        });
    }

    public void startGossiping() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendGossip();
            }
        }, 0, Long.parseLong(PropertyProvider.getProperty("GOSSIP_INTERVAL")));
    }

    public void stopGossiping() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void sendGossip() {
        //select a node which are not synced recently
        Node selectedNode = null;
        List<Node> allNodes = Router.getInstance().getAllNodes();
        for (Node node : allNodes) {
            if (!syncTimestamps.containsKey(node)) {
                selectedNode = node;
                break;
            } else if (selectedNode == null) {
                selectedNode = node;
            } else {
                if (syncTimestamps.get(node) < syncTimestamps.get(selectedNode)) {
                    selectedNode = node;
                }
            }
        }

        if(selectedNode == null){
            System.out.println("No nodes found for gossipping");
            return;
        }else {
            System.out.println(selectedNode.toString() + " selected for gossiping");
        }

        //share comment store
        MessageHandler handler = MessageHandler.getInstance();
        PostsMessage message = new PostsMessage(PostStore.getPosts());
        message.setSourceIP(handler.getLocalIP());
        message.setSourcePort(handler.getLocalPort());
        message.setDestinationIP(selectedNode.getIp());
        message.setDestinationPort(selectedNode.getPort());
//        handler.send(message);
        System.out.println("Gossip done with " + selectedNode.getNodeID());

        syncTimestamps.put(selectedNode, System.currentTimeMillis());
    }
}
