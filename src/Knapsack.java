import java.util.ArrayList;
import java.util.List;

class Knapsack {

    static List<Integer> knapsack(int num, int Money, int[] cost, int[] weight) {

        /*
            ナップサック問題を解く
            引数 : 品物の個数, 使うことのできる最大のおかね, 品物の金額リスト, 品物の価値(現時点では全て1)
            戻り値 : 選ぶべき品物のインデックス(同じ値段の品物があった場合，前のものが優先される仕様である)
        */

        int[][] dp = new int[num + 1][Money + 1]; // DPテーブル
        int[][] prev = new int[num + 1][Money + 1]; // DP復元用テーブル(DPの遷移を記録)
        List<Integer> res = new ArrayList<Integer>(); // 選ぶべき品物リスト

        for (int i = 0; i < num; ++i) {
            for (int j = 0; j <= Money; ++j) {
    
                // i 番目の品物を選ぶ場合
                if (j >= cost[i]) {
                    if (dp[i + 1][j] < dp[i][ j - cost[i] ] + weight[i]) {
                        dp[i + 1][j] = dp[i][ j - cost[i] ] + weight[i];
                        prev[i + 1][j] = j - cost[i];
                    }
                }
    
                // i 番目の品物を選ばない場合
                if (dp[i + 1][j] < dp[i][j]) {
                    dp[i + 1][j] = dp[i][j];
                    prev[i + 1][j] = j;
                }
            }
        }
        // DPテーブルから選んだ品物を復元
        int currnt = Money;
        for (int i = num - 1; i >= 0; --i) {
            // 選んでいた場合
            if (prev[i + 1][currnt] == currnt - cost[i]) {
                // これが選んだ品物
                // System.out.println( i + " th item (cost = " + cost[i] + ", weight = " + weight[i] + ")");
                res.add(i);
            }

            // 復元テーブルをたどる
            currnt = prev[i + 1][currnt];
        }
        // 適切に選んだときの金額はdp[num][Money]
        return res;
    }

    /**
     * ナップサック問題を応用して、値段を超えない適切な小皿の組み合わせを求める
     * @param mealDataList 小皿のリストデータ
     * @param Money 小皿に使える最大価格
     * @return 適切な組み合わせの小皿indexリスト
     */
    public static List<Integer> filterVal(final List<MealData> mealDataList, final int Money) {
        
        int[] cost = new int[mealDataList.size()]; // 品物の金額
        int[] weight = new int[mealDataList.size()]; // 品物の価値(現時点では全て1だが，拡張性を持たせている)

        int pos = 0;
        for (MealData data : mealDataList) {
            cost[pos] = data.getValue();
            weight[pos] = 1;
            pos += 1;
        }
        List<Integer> res = knapsack(mealDataList.size(), Money, cost, weight);
        
        return res;
    }

}