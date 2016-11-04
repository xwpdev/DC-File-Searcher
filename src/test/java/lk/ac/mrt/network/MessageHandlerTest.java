package lk.ac.mrt.network;

import junit.framework.TestCase;
import lk.ac.mrt.common.PropertyProvider;

/**
 * Created by chamika on 11/4/16.
 */
public class MessageHandlerTest extends TestCase {
    public void testSend() throws Exception {
        MessageHandler instance = MessageHandler.getInstance();
        RegisterMessage message = new RegisterMessage();
        message.setSourceIP("192.168.10.100");
        message.setSourcePort(8080);
        message.setUsername("abcd");

        message.setDestinationIP(PropertyProvider.getProperty("REG_IP"));
        message.setDestinationPort(Integer.parseInt(PropertyProvider.getProperty("REG_PORT")));


        for(int i=0;i<10;i++) {
//            Thread.sleep(500);
            message.setSourcePort(message.getSourcePort() + 1 );
            message.setUsername("abcd" +String.valueOf(message.getSourcePort()));
//            instance.send(message);
        }
    }

}