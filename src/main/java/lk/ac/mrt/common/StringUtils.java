package lk.ac.mrt.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ac.mrt.comment.Comment;
import lk.ac.mrt.comment.Rank;
import lk.ac.mrt.comment.RankCalculator;
import lk.ac.mrt.comment.Viewable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static void generateCommentListView(StringBuilder sb, ArrayList<Comment> tempComments) {
        for (Comment tempComment : tempComments) {
            String view = tempComment.generateView();
            String[] split = view.split(Viewable.NEW_LINE);
            for (String s : split) {
                sb.append("\t");
                sb.append(s);
                sb.append(Viewable.NEW_LINE);
            }
        }
    }

    public static void generateRating(StringBuilder sb, List<Rank> ranks) {
        sb.append("Rating    : ");
        int rating = RankCalculator.calculateAverageRank(ranks);
        if (rating == -1) {
            sb.append(" -- Not rated --");
        } else {
            sb.append(rating);
            sb.append(" (");
            sb.append(ranks.size());
            if (ranks.size() == 1) {
                sb.append(" user) ");
            } else {
                sb.append(" users) ");
            }
        }
    }

}

