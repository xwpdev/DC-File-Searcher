package lk.ac.mrt.comment;

import java.util.List;

/**
 * Created by dinu on 1/25/17.
 */
public class RankCalculator {

    public static int calculateAverageRank(List<Rank> rankList){
        int average = -1;
        int sum = 0;
        if(rankList != null && !rankList.isEmpty()){
            for(Rank rank:rankList){
                sum +=rank.getRank();
            }
            average = sum/rankList.size();
        }
        return average;
    }
}
