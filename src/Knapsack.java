import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Knapsack {

    static int knapsack(int num, int maxValue, int[] cost, int[] weight){
        int[][] dp = new int[num + 1][maxValue + 1];
        for(int i = 1; i <= num; i++) {
            for(int j = 1; j <= maxValue; j++) {
                if(j - cost[i] >= 0) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - cost[i]] + weight[i]);
                }
                dp[i][j] = Math.max(dp[i][j], dp[i - 1][j]);
            }
        }
        return dp[num][maxValue];
    }

    public static List<MealData> filterVal(final List<MealData> mealDataList, final int maxValue) {
        final List<MealData> filterList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getValue() <= maxValue)
                        .collect(Collectors.toList());
        
        int[] weight = new int[mealDataList.size() + 1];
        int[] value = new int[mealDataList.size() + 1];

        int pos = 1;
        for (MealData data : mealDataList) {
            weight[pos] = 1;
            value[pos] = data.getValue();
            pos += 1;
        }
        int res = knapsack(mealDataList.size(), maxValue, value, weight);
        System.out.println(res);

        return filterList;
    }

}
