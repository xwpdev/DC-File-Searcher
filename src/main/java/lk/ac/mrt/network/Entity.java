package lk.ac.mrt.network;

/**
 * Created by chamika on 11/3/16.
 */
public abstract class Entity {
    protected final static char WHITESPACE = ' ';

    public abstract String marshall();

    public abstract void unmarshall(String messsageData);

    protected String appendAll(Object... obj){
        return appendWithSeparator(WHITESPACE, obj);
    }

    protected String appendWithSeparator(char separator, Object... obj) {
        if (obj == null || obj.length == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < obj.length; i++) {
                if (i != 0) {
                    sb.append(separator);
                }
                sb.append(obj[i]);

            }
            return sb.toString();
        }
    }

}
