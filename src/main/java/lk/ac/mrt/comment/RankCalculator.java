package lk.ac.mrt.comment;

import java.util.List;

/**
 * Created by dinu on 1/25/17.
 */
public class RankCalculator {

    public static int calculateAverageRank(List<Rank> rankList){
        int average = 0;
        int sum = 0;
        for(Rank rank:rankList){
            sum +=rank.getRank();
        }
        average = sum/rankList.size();
        return average;
    }
}
