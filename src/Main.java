import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;


public class Main {
    public static void main(String[] args) {
        // csvファイルを読み込む
        List<MealData> mealDataList = loadCsv("mealData.csv");

        // 適当に絞り込み要件を定義 (本当はユーザーに入力させたい)
        String priorityPlace = "リンク"; // リンク優先
        int maxValue = 100;
        int priorityColumn = 4; // カロリー優先

        // maxValue以下のものだけ出力テスト(→完成している)
        List<MealData> RecommendList = new ArrayList<>();
        RecommendList = Knapsack.filterVal(mealDataList, 500);
        


        // 計算量を減らすため、先に場所限定と価格以下のフィルタをかける
        List<MealData> filterList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getPlace().equals(priorityPlace))
                        .filter(mealData -> mealData.getValue() <= maxValue)
                        .collect(Collectors.toList());

        // 確認出力
        filterList.stream().forEach(data -> data.dump());

        // 残ったものの情報を増大させる, まずは米なしメインに米を足してメインにする
        // 米なしメインのみ抽出, 米のみ抽出
        List<MealData> noRiceMainList =
                filterList.stream()
                        .filter(mealData -> mealData.getCategory().equals("米なしメイン"))
                        .collect(Collectors.toList());

        List<MealData> riceList =
                filterList.stream()
                        .filter(mealData -> mealData.getCategory().equals("米"))
                        .collect(Collectors.toList());

        // 米なしメイン+米=メイン を作成する




        // 全通りのリストができたと仮定する

        //answerList.sort

        // 求めたリストを出力する

    }

    // csvファイルを読み込む
    public static List<MealData> loadCsv(String filename) {
        File file = new File(filename);
        List<MealData> mealDataList = new ArrayList<>(); // メインのデータとなる

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            List<String[]> csvData = new ArrayList<>(); // csvファイルのデータ分割に必要
            int columnIndex = 0;
            while ((line=br.readLine())!=null) {
                if(columnIndex==0) {
                    //String[] itemName = line.split(","); // 最初だけ項目を保持
                } else {
                    String[] cols = line.split(","); // 2回目以降はデータを決まった型で保持
                    csvData.add(cols);
                }
                columnIndex++;
            }
            // 読み込みデータの表示
            for (String[] row : csvData) {
                MealData md = new MealData(
                        row[0], row[1], row[2],
                        Integer.parseInt(row[3]), Double.parseDouble(row[4]),
                        Double.parseDouble(row[5]), Double.parseDouble(row[6]),
                        Double.parseDouble(row[7]), Double.parseDouble(row[8]),
                        Integer.parseInt(row[9]), Integer.parseInt(row[10])
                );
                mealDataList.add(md);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mealDataList;
    }

}
