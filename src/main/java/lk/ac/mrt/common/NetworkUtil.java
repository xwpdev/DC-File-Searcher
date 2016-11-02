package lk.ac.mrt.common;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Created by dinu on 11/3/16.
 */
public class NetworkUtil {

    public static String getIP(){

        String ip = "";
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }
}
