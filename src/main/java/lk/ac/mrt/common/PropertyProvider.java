package lk.ac.mrt.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chamika on 11/3/16.
 */
public class PropertyProvider {

    private static Properties prop;

    public static String getProperty(String key) {
        if (prop == null) {
            initProperties();
        }
        return prop.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (prop == null) {
            initProperties();
        }
        return prop.getProperty(key, defaultValue);
    }

    private static void initProperties() {
        prop = new Properties();

        InputStream input = null;
        try {

            input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
