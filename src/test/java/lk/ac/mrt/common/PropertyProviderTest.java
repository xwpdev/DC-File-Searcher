package lk.ac.mrt.common;

import junit.framework.TestCase;

/**
 * Created by chamika on 11/3/16.
 */
public class PropertyProviderTest extends TestCase {
    public void testGetProperty() throws Exception {
        String username = PropertyProvider.getProperty("USERNAME");
        assertEquals("cads_node1",username);
    }

}