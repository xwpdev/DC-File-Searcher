package lk.ac.mrt.search;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by chamika on 11/3/16.
 */
public class FileProvider {

    /**
     * Read allfiles.txt and return limitted random file names
     *
     * @param limit
     * @return
     */
    public static FilesList getRandomFileList(int limit) {
        List<String> list = readLines("allfiles.txt");

        if (list.size() <= limit) {
            FilesList filesList = new FilesList();
            filesList.setFileNames(list);
            System.out.println("Nothing to random. All files selected");
            filesList.printFileNames();
            return filesList;
        }

        Random rand = new Random();
        Set<String> set = new HashSet<String>();
        while (set.size() < limit) {
            int i = rand.nextInt(list.size());
            set.add(list.get(i));
        }

        FilesList filesList = new FilesList();
        filesList.setFileNames(new ArrayList<String >(set));
        System.out.println("Randomly selected following files.");
        filesList.printFileNames();
        return filesList;
    }

    /**
     * Read files.txt and return all files containing in the file
     *
     * @return
     */
    public static FilesList getConfiguredFiles() {
        FilesList filesList = new FilesList();
        filesList.setFileNames(readLines("files.txt"));
        System.out.println("Configured to select following files.");
        filesList.printFileNames();
        return filesList;
    }

    private static List<String> readLines(String fileName) {
        List<String> files = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    files.add(line.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return files;
    }
}
