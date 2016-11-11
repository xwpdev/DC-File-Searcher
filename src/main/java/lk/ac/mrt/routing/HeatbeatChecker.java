package lk.ac.mrt.routing;

import lk.ac.mrt.common.PropertyProvider;
import lk.ac.mrt.network.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chamika on 11/11/16.
 */
public class HeatbeatChecker {

    private static HeatbeatChecker INSTANCE;
    private Timer timer;

    public static HeatbeatChecker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HeatbeatChecker();
        }
        return INSTANCE;
    }

    private HeatbeatChecker() {
    }

    public void startChecking() {
        if(timer != null ){
            timer.cancel();
        }
        timer = new Timer();
        final int[] time = {0};
        final int limit = Integer.parseInt(PropertyProvider.getProperty("EVICT_TIMES"));
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendHeartbeats();
                ++time[0];
                if(time[0] % limit == 0){
                    Router.getInstance().clearInactive();
                    time[0] = 0;
                }
            }
        }, Long.parseLong(PropertyProvider.getProperty("HEARTBEAT_DELAY"))*1000, Long.parseLong(PropertyProvider.getProperty("HEARTBEAT_INTERVAL")) * 1000);
    }

    public void stopChecking(){
        if(timer != null) {
            timer.cancel();
        }
    }

    private void sendHeartbeats() {
        List<Node> allNodes = Router.getInstance().getAllNodes();
        MessageHandler handler = MessageHandler.getInstance();
        HeartbeatMessage message = new HeartbeatMessage();
        for (Node node : allNodes) {
            message.setSourceIP(handler.getLocalIP());
            message.setSourcePort(handler.getLocalPort());
            message.setDestinationIP(node.getIp());
            message.setDestinationPort(node.getPort());
            handler.send(message);
        }
    }
}
