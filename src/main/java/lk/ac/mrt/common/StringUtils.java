package lk.ac.mrt.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by chamika on 1/25/17.
 */
public class StringUtils {

    /**
     * Serializes object to JSON string
     *
     * @param obj
     * @param pretty
     * @return
     * @throws IOException
     */
    public static String toJson(Object obj, boolean pretty) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false);
        if (pretty) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }
        return mapper.writeValueAsString(obj);

    }

    public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

}

