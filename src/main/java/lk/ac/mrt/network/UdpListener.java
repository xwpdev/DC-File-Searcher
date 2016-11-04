package lk.ac.mrt.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by chamika on 11/4/16.
 */
public class UdpListener extends Thread {

    private boolean running;
    private int localPort;

    @Override
    public void run() {
        super.run();
        running = true;

        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(localPort);

            while(running){
                byte[] buffer = new byte[1024];
                DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
                try {
                    datagramSocket.receive(incomingPacket);

                    InetAddress ipAddress = incomingPacket.getAddress();
                    int port = incomingPacket.getPort();

                    String msg = new String(buffer);
                    System.out.println("Listener received: " + msg + " from " + ipAddress + ":" + port);

                    int length = Integer.parseInt(msg.substring(0, MessageHandler.MSG_LENGTH));

                    Message message= MessageHandler.getInstance().handleMessage(msg.substring(0, length));

                    MessageListener listener = MessageHandler.getInstance().getListener(message.type);

                    if(listener == null){
                        // do nothing. No need of responding
                    }else{
                        Response response = listener.onMessageReceived(message);
                        if( response != null) {
                            String finalString = MessageHandler.prepareForSending(response);
                            byte[] bytes = finalString.getBytes();
                            DatagramPacket replyPacket = new DatagramPacket(bytes,bytes.length,InetAddress.getByName(response.getDestinationIP()),response.getDestinationPort());
                            datagramSocket.send(replyPacket);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }
}
