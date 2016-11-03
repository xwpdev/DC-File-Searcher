package lk.ac.mrt.search;

import lk.ac.mrt.network.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sajitha on 11/3/2016.
 */
public class MessageMap {

    private Map<String, Message> messageMap;

    public MessageMap(){
        this.messageMap = new HashMap<String, Message>();
    }

    public Map<String, Message> getMessageMap() {
        return messageMap;
    }

    public void setMessageMap(Map<String, Message> messageMap) {
        this.messageMap = messageMap;
    }


}
