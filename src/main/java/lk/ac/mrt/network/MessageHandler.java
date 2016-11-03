package lk.ac.mrt.network;

import lk.ac.mrt.common.NetworkUtil;
import lk.ac.mrt.common.PropertyProvider;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageHandler {

    private static final int MSG_LENGTH = 4;
    private static MessageHandler instance;

    private String localIP;
    private int localPort;
    private boolean initialized;

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

    /**
     * Send a message through UDP and get the response
     *
     * @param msg
     * @return
     */
    public Response send(Message msg) {

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

    private Response handleResponse(String responseLine) {
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

    /**
     * Open the port and listen
     *
     * @param port
     */
    public void startListening(int port) {

    }

    /**
     * Close the port which is listening initiated by {@link #startListening(int)}
     */
    public void stopListening() {

    }


    public void registerForReceiving(MessageType type, MessageListener listener) {

    }

    public void setLocalDetails(Message message) {
        if (initialized) {
            message.setSourceIP(localIP);
            message.setSourcePort(localPort);
        } else {
            new Exception("Network IP or port not initialized").printStackTrace();
        }
    }

    private static String prepareForSending(Entity entity) {
        String txt = entity.marshall();
        return String.format("%04d %s", txt.length() + 5, txt);
    }

}
