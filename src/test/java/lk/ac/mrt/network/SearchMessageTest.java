package lk.ac.mrt.network;

import junit.framework.TestCase;

/**
 * Created by chamika on 11/3/16.
 */
public class SearchMessageTest extends TestCase {
    public void testMarshall() throws Exception {
        //SER IP port file_name hops
        //SER 192.168.1.100 8080 harry 10
        SearchMessage message = new SearchMessage();
        message.setSourceIP("192.168.1.100");
        message.setSourcePort(8080);
        message.setKeyword("harry");
        message.setHopCount(10);
        assertEquals("SER 192.168.1.100 8080 harry 10",message.marshall());
    }

    public void testUnmarshall() throws Exception {
        String txt = "SER 192.168.1.100 8080 harry 10";
        SearchMessage message = new SearchMessage();
        message.unmarshall(txt);
        assertEquals("192.168.1.100",message.getSourceIP());
        assertEquals(8080,message.getSourcePort());
        assertEquals("harry",message.getKeyword());
        assertEquals(10,message.getHopCount());
    }

}