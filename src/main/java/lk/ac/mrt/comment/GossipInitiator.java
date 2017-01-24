package lk.ac.mrt.comment;

import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.MessageHandler;
import lk.ac.mrt.routing.Node;
import lk.ac.mrt.routing.Router;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chamika on 1/24/17.
 */
public class GossipInitiator {
    private static GossipInitiator INSTANCE;
    private Timer timer;

    public static GossipInitiator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GossipInitiator();
        }
        return INSTANCE;
    }

    private GossipInitiator() {
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
        //select a node which are not synced
        //share comment store

        List<Node> allNodes = Router.getInstance().getAllNodes();
        MessageHandler handler = MessageHandler.getInstance();
        Posts message = new Posts();
        for (Node node : allNodes) {
            message.setSourceIP(handler.getLocalIP());
            message.setSourcePort(handler.getLocalPort());
            message.setDestinationIP(node.getIp());
            message.setDestinationPort(node.getPort());
            handler.send(message);
        }
    }
}
