import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Knapsack {

    static List<Integer> knapsack(int num, int Money, int[] cost, int[] weight){
        int[][] dp = new int[num + 1][Money + 1];
        int[][] prev_w = new int[num + 1][Money + 1];
        List<Integer> res = new ArrayList<Integer>();

        for (int i = 0; i < num; ++i) {
            for (int j = 0; j <= Money; ++j) {
    
                // i 番目の品物を選ぶ場合
                if (j >= cost[i]) {
                    if (dp[i + 1][j] < dp[i][j - cost[i] ] + weight[i]) {
                        dp[i + 1][j] = dp[i][j - cost[i] ] + weight[i];
                        prev_w[i + 1][j] = j - cost[i];
                    }
                }
    
                // i 番目の品物を選ばない場合
                if (dp[i + 1][j] < dp[i][j]) {
                    dp[i + 1][j] = dp[i][j];
                    prev_w[i + 1][j] = j;
                }
            }
        }
        // 復元
        int cur_w = Money;
        for (int i = num - 1; i >= 0; --i) {
            // 選んでいた場合
            if (prev_w[i + 1][cur_w] == cur_w - cost[i]) {
                // System.out.println( i + " th item (cost = " + cost[i] + ", weight = " + weight[i] + ")");
                res.add(i);
            }

            // 復元テーブルをたどる
            cur_w = prev_w[i + 1][cur_w];
        }
        // 適切に選んだときの金額はdp[num][Money]
        return res;
    }

    public static List<Integer> filterVal(final List<MealData> mealDataList, final int Money) {
        
        int[] weight = new int[mealDataList.size()];
        int[] value = new int[mealDataList.size()];

        int pos = 0;
        for (MealData data : mealDataList) {
            weight[pos] = 1;
            value[pos] = data.getValue();
            pos += 1;
        }
        List<Integer> res = knapsack(mealDataList.size(), Money, value, weight);
        // for (Integer i : res) {
        //     System.out.println(i);
        // }
        return res;
    }

}