package lk.ac.mrt.network;

/**
 * Message listening interface for registering various message types
 */
public interface MessageListener {
    Response onMessageReceived(Message message);
}
