package lk.ac.mrt.network;

import lk.ac.mrt.common.NetworkUtil;
import lk.ac.mrt.common.PropertyProvider;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class MessageHandler {

    public static final int MSG_LENGTH = 4;
    private static MessageHandler instance;

    private String localIP;
    private int localPort;
    private boolean initialized;
    private UdpListener udpListener;
    private HashMap<MessageType, MessageListener> registeredListeners = new HashMap<>();

    public static MessageHandler getInstance() {
        if (instance == null) {
            instance = new MessageHandler();
        }
        return instance;
    }

    public MessageHandler() {
        this.localIP = NetworkUtil.getIP();
        this.localPort = Integer.parseInt(PropertyProvider.getProperty("PORT"));
        if (localIP != null && !localIP.isEmpty() && localPort > 0) {
            initialized = true;
        }
    }

    // AHESH
    /**
     * Send a UDP message
     * @param ipAddress
     * @param port
     * @param message
     * @return
     */
    public void sendUDPMsg(String ipAddress,int port,String message) {
        try {
            DatagramSocket ds = new DatagramSocket();
            InetAddress ip = InetAddress.getByName(ipAddress);
            DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), ip, port);
            ds.send(dp);
            ds.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Send a message through UDP and get the response
     *
     * @param msg
     * @return
     */
    public Response send(Message msg) {

        if(msg.type != MessageType.REGISTER){
            //stop UDP listening to accept response
            boolean listening = isListening();
            if(listening){
                stopListening();
            }

            sendUDPMsg(msg.getDestinationIP(),msg.getDestinationPort(),prepareForSending(msg));
            byte[] buffer = new byte[1024];
            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            try {
                DatagramSocket datagramSocket = new DatagramSocket(localPort);
                datagramSocket.receive(incomingPacket);

                InetAddress ipAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();

                String tempMsg = new String(buffer);
                System.out.println("Listener received: " + tempMsg + " from " + ipAddress + ":" + port);
                int length = Integer.parseInt(tempMsg.substring(0, MessageHandler.MSG_LENGTH));

                return MessageHandler.getInstance().handleResponse(tempMsg.substring(0, length));
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(listening){
                    startListening();
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
        } else if (unmarshallText.startsWith(MessageType.SEARCH.code())) {
            message = new SearchMessage();
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

    /**
     * Open the port and listen
     *
     */
    public void startListening() {
        if (udpListener == null) {
            udpListener = new UdpListener();
            udpListener.setLocalPort(localPort);
        }

        udpListener.start();
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
        registeredListeners.put(type,listener);
    }

    public void setLocalDetails(Message message) {
        if (initialized) {
            message.setSourceIP(localIP);
            message.setSourcePort(localPort);
        } else {
            new Exception("Network IP or port not initialized").printStackTrace();
        }
    }

    public static String prepareForSending(Entity entity) {
        String txt = entity.marshall();
        return String.format("%04d %s", txt.length() + 5, txt);
    }

    public MessageListener getListener(MessageType type){
        return registeredListeners.get(type);
    }

}
