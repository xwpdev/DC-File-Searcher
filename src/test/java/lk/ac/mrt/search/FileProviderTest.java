package lk.ac.mrt.search;

import junit.framework.TestCase;

/**
 * Created by chamika on 11/3/16.
 */
public class FileProviderTest extends TestCase {
    public void testGetRandomFileList() throws Exception {
        FilesList randomFileList = FileProvider.getRandomFileList(5);
        assertEquals(5, randomFileList.getFileNames().size());
    }

    public void testGetConfiguredFiles() throws Exception {
        FilesList configuredFiles = FileProvider.getConfiguredFiles();
        assertEquals(5, configuredFiles.getFileNames().size());
    }

}