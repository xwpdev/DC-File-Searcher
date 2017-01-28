package lk.ac.mrt.network;

import lk.ac.mrt.comment.PostsMessage;
import lk.ac.mrt.comment.PostsResponse;
import lk.ac.mrt.common.Constants;
import lk.ac.mrt.common.NetworkUtil;
import lk.ac.mrt.common.PropertyProvider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MessageHandler {

    public static final int MSG_LENGTH = 4;
    private static MessageHandler instance;



    private String localIP;
    private int localPort;
    private boolean initialized;
    private UdpListener udpListener;
    private HashMap<String, MessageListener> registeredListeners = new HashMap<String, MessageListener>();

    private DatagramSocket datagramSocket;

    public MessageHandler() {
        this.localIP = NetworkUtil.getIP();
        this.localPort = Integer.parseInt(PropertyProvider.getProperty("PORT"));
        if (localIP != null && !localIP.isEmpty() && localPort > 0) {
            initialized = true;
        }

        if(initialized){
            try {
                datagramSocket = new DatagramSocket(null);
                datagramSocket.bind(new InetSocketAddress(localIP, localPort));
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    public static MessageHandler getInstance() {
        if (instance == null) {
            instance = new MessageHandler();
        }
        return instance;
    }

    public static Response handleResponse(String responseLine) {
        int length = Integer.parseInt(responseLine.substring(0, MSG_LENGTH));
        int startIndex = MSG_LENGTH + 1;
        String unmarshallText = responseLine.substring(startIndex, length);

        Response response = null;
        if (unmarshallText.startsWith(ResponseType.ERROR.code())) {
            response = new ErrorResponse();
        } else if (unmarshallText.startsWith(ResponseType.REGISTER.code())) {
            response = new RegisterResponse();
        } else if (unmarshallText.startsWith(ResponseType.UNREGISTER.code())) {
            response = new UnRegisterResponse();
        } else if (unmarshallText.startsWith(ResponseType.JOIN.code())) {
            response = new JoinResponse();
        } else if (unmarshallText.startsWith(ResponseType.LEAVE.code())) {
            response = new LeaveResponse();
        } else if (unmarshallText.startsWith(ResponseType.SEARCH.code())) {
            response = new SearchResponse();
        } else if (unmarshallText.startsWith(ResponseType.LIVE.code())) {
            response = new HeartbeatResponse();
        } else if (unmarshallText.startsWith(ResponseType.GOSSOK.code())) {
            response = new PostsResponse();
        }

        if (response != null) {
            try {
                response.unmarshall(unmarshallText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (response == null) {
            response = new ErrorResponse();
            ((ErrorResponse) response).setValue("Unknown error");
        }


        return response;
    }

    public static Message handleMessage(String messageLine) {
        int length = Integer.parseInt(messageLine.substring(0, MSG_LENGTH));
        int startIndex = MSG_LENGTH + 1;
        String unmarshallText = messageLine.substring(startIndex, length);

        Message message = null;
        if (unmarshallText.startsWith(MessageType.REGISTER.code())) {
            message = new RegisterMessage();
        } else if (unmarshallText.startsWith(MessageType.UNREGISTER.code())) {
            message = new UnRegisterMessage();
        } else if (unmarshallText.startsWith(MessageType.JOIN.code())) {
            message = new JoinMessage();
        } else if (unmarshallText.startsWith(MessageType.LEAVE.code())) {
            message = new LeaveMessage();
        } else if (unmarshallText.startsWith(MessageType.SEARCH.code()) && !unmarshallText.startsWith(ResponseType.SEARCH.code())) {
            message = new SearchMessage();
        } else if (unmarshallText.startsWith(MessageType.LIVE.code()) && !unmarshallText.startsWith(ResponseType.LIVE.code())) {
            message = new HeartbeatMessage();
        } else if (unmarshallText.startsWith(MessageType.GOSSIP.code())) {
            message = new PostsMessage();
        }

        if (message != null) {
            try {
                message.unmarshall(unmarshallText);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return message;
    }

    public static String prepareForSending(Entity entity) {
        String txt = entity.marshall();
        return String.format("%04d %s", txt.length() + 5, txt);
    }

    public String getLocalIP() {
        return localIP;
    }
    // AHESH

    public void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    /**
     * Send a UDP message
     * @param ipAddress
     * @param port
     * @param message
     * @return
     */
    public void sendUDPMsg(String ipAddress,int port,String message) {
        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(ipAddress);
            DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), ip, port);
            ds.send(dp);
//            System.out.println("Sent UDP from port:" + ds.getLocalPort() + " to:" + ip + ":" + port + " Msg:" + message);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(ds != null) {
                ds.close();
            }
        }
    }

    /**
     * Send a message through UDP and get the response
     *
     * @param msg
     * @return
     */
    public Response send(final Message msg) {

        if(msg.type != MessageType.REGISTER && msg.type != MessageType.UNREGISTER){
            //stop UDP listening to accept response
            final boolean listening = isListening();
            final boolean needResponse = !(msg.type == MessageType.SEARCH || msg.type == MessageType.LIVE || msg.type == MessageType.GOSSIP);
            if(listening && needResponse){
                stopListening();
            }

            //
            if(!needResponse){
                //no response can directly send message
                sendUDPMsg(msg.getDestinationIP(),msg.getDestinationPort(),prepareForSending(msg));
            }else {
                // if need response, we have to start receive and later send the message
                final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                executor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        sendUDPMsg(msg.getDestinationIP(), msg.getDestinationPort(), prepareForSending(msg));
                    }
                }, 1, TimeUnit.SECONDS);
            }

            if(needResponse) {
                try {
                    boolean hasErrors = false;
                    Response response = null;
                    do {
                        byte[] buffer = new byte[Constants.MESSAGE_LENGTH];
                        DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);

                        datagramSocket.setSoTimeout(10000);
                        datagramSocket.receive(incomingPacket);

                        InetAddress ipAddress = incomingPacket.getAddress();
                        int port = incomingPacket.getPort();

                        String tempMsg = new String(buffer);
                        int length = Integer.parseInt(tempMsg.substring(0, MessageHandler.MSG_LENGTH));

                        String realMsg = tempMsg.substring(0, length);
                        System.out.println("Listener received: " + realMsg + " from " + ipAddress + ":" + port);
                        response = MessageHandler.getInstance().handleResponse(realMsg);
                        if (msg instanceof JoinMessage && !(response instanceof JoinResponse)) {
                            hasErrors = true;
                        }
                    } while (hasErrors);
                    return response;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
//                    if (datagramSocket == null) {
//                        datagramSocket.close();
//                    }
                    try {
                        datagramSocket.setSoTimeout(0);
                    } catch (SocketException e1) {
                        e1.printStackTrace();
                    }
                    if (listening && needResponse) {
                        startListening();
                    }
                }
            }

            return null;
        }else {

            Socket socket = null;
            DataOutputStream os = null;
            BufferedReader is = null;

            String destinationIP = msg.getDestinationIP();
            int destinationPort = msg.getDestinationPort();
            if (destinationIP == null || destinationIP.isEmpty() || destinationPort <= 0) {
                System.err.println("Invalid destination data. host:port=" + destinationIP + ":" + destinationPort);
                return null;
            }
            try {
                socket = new Socket(destinationIP, destinationPort);
                os = new DataOutputStream(socket.getOutputStream());
                is = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String finalString = prepareForSending(msg);
                System.out.println("Sending msg:" + finalString);
                os.write(finalString.getBytes());


                String responseLine = is.readLine();
                System.out.println("Response: " + responseLine);

                return handleResponse(responseLine);

            } catch (UnknownHostException e) {
                System.err.println("Unable to connect to host: " + destinationIP);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: " + destinationIP);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }

    /**
     * Open the port and listen
     *
     */
    public void startListening() {
        if (udpListener == null) {
            udpListener = new UdpListener();
            udpListener.setLocalPort(localPort);
        }

        if(!udpListener.isRunning()) {
            udpListener.start();
        }
    }

    /**
     * Close the port which is listening initiated by {@link #startListening()}
     */
    public void stopListening() {
        if(udpListener != null){
            udpListener.setRunning(false);
        }
    }

    public boolean isListening(){
        return (udpListener != null && udpListener.isRunning());
    }

    public void registerForReceiving(MessageType type, MessageListener listener) {
        registeredListeners.put(type.code(),listener);
    }

    public void registerForReceiving(ResponseType type, MessageListener listener) {
        registeredListeners.put(type.code(),listener);
    }

    public void setLocalDetails(Message message) {
        if (initialized) {
            message.setSourceIP(localIP);
            message.setSourcePort(localPort);
        } else {
            new Exception("Network IP or port not initialized").printStackTrace();
        }
    }

    public void setLocalDetails(Response response) {
        if (initialized) {
            response.setSourceIP(localIP);
            response.setSourcePort(localPort);
        } else {
            new Exception("Network IP or port not initialized").printStackTrace();
        }
    }

    public MessageListener getListener(MessageType type){
        return registeredListeners.get(type.code());
    }

    public MessageListener getListener(ResponseType type){
        return registeredListeners.get(type.code());
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }
}
