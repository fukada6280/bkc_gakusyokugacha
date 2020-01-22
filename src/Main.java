import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        // csvファイルを読み込む
        List<MealData> mealDataList = loadCsv("mealData.csv");

        // 適当に絞り込み要件を定義 (本当はユーザーに入力させたい)
        String priorityPlace = "リンク"; // リンク優先
        int maxValue = 500;
        int priorityColumn = 4; // カロリー優先


        // 指定された場所の学食の価格リスト, 重複は削除 (ryuseiくんへ受け渡す)
        List<Integer> valueList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getPlace().equals(priorityPlace))
                        .map(mealData -> mealData.getValue())
                        .distinct()
                        .collect(Collectors.toList());

        // 小鉢に充てるお金をもつリストを作成
        List<Integer> moneyApplyInKozaraList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getPlace().equals(priorityPlace))
                        .map(mealData -> maxValue - mealData.getValue())
                        .collect(Collectors.toList());

        List<List<Integer>> RecommendList = new ArrayList<>();
        for (Integer moneyApplyInKozara : moneyApplyInKozaraList) {
            List<Integer> tmp = new ArrayList<>();
            tmp = Knapsack.filterVal(valueList, moneyApplyInKozara);
            RecommendList.add(tmp);
        }

        // 正しいか確認
        RecommendList.stream()
                .forEach(System.out::println);


        /* お試し実行 価格を昇順に並べる
        /mealDataList.stream()
                .sorted(Comparator.comparingInt(MealData::getValue))
                .forEach(System.out::println);
         */

        /* お試し実行 価格を降順に並べる
        mealDataList.stream()
                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                .forEach(System.out::println);
         */
    }

    /**
     * csvファイルを読み込む
     * @param filename 開きたいcsvファイルの名前
     * @return MealData型のリスト
      */
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


    /*
     * メイン+（必要に応じて米）+(必要に応じて小皿１つ)の組み合わせで条件に合うものを出力
     * @param mealDataList 元となるデータリスト
     * @param priorityPlace 優先する場所
     * @param maxValue 制限したい価格
     * @return 条件に見合うランチセットのリスト

    public static List<MealData> makeLunchSetList(List<MealData> mealDataList, String priorityPlace, int maxValue) {
        //
        // 計算量を減らすため、先に場所限定と価格以下のフィルタをかける
        List<MealData> filterList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getPlace().equals(priorityPlace))
                        .filter(mealData -> mealData.getValue() <= maxValue)
                        .collect(Collectors.toList());

        // 残ったものの情報を増大させる, まずは米なしメインに米を足してメインにする
        // 米なしメインのみ抽出, 米のみ抽出, メインのみ抽出, 小皿のみ抽出
        List<MealData> noRiceMainList =
                filterList.stream()
                        .filter(mealData -> mealData.getCategory().equals("米なしメイン"))
                        .collect(Collectors.toList());

        List<MealData> riceList =
                filterList.stream()
                        .filter(mealData -> mealData.getCategory().equals("米"))
                        .collect(Collectors.toList());

        List<MealData> mainList =
                filterList.stream()
                        .filter(mealData -> mealData.getCategory().equals("メイン"))
                        .collect(Collectors.toList());

        List<MealData> kozaraList =
                filterList.stream()
                        .filter(mealData -> mealData.getCategory().equals("小皿"))
                        .collect(Collectors.toList());

        // 米なしメイン+米=メイン を作成する
        for (MealData noRiceMainData : noRiceMainList) {
            for (MealData riceData : riceList) {
                MealData mealData = noRiceMainData.combine(riceData); // 米なしメイン + 米 = メイン を作成
                mainList.add(mealData);
            }
        }


        // ryuseiくんへ受け渡す
        // 小鉢に充てるお金をもつリストを作成
        List<Integer> moneyApplyInKozaraList =
                mainList.stream()
                        .map(mealData -> maxValue - mealData.getValue())
                        .collect(Collectors.toList());

        List<List<Integer>> RecommendList = new ArrayList<>();

        for (Integer moneyApplyInKozara : moneyApplyInKozaraList) {
            List<Integer> tmp = new ArrayList<>();
            tmp = Knapsack.filterVal(kozaraList, moneyApplyInKozara);
            RecommendList.add(tmp);
        }

        //
        RecommendList.stream()
                .forEach(System.out::println);


        // 小皿を追加する
        List<MealData> allPatternList = new ArrayList<>();
        for (MealData main : mainList) {
            for (MealData kozara : kozaraList) {
                MealData mealData = main.combine(kozara);
                allPatternList.add(mealData);
            }
        }

        // 価格でフィルタ, 価格を大きい順でソート
        List<MealData> resultList =
                allPatternList.stream()
                        .filter(mealData -> mealData.getValue() <= maxValue)
                        .collect(Collectors.toList());

        resultList = sortValue(resultList);

        // 確認
        //System.out.println("------------結果を出力--------------");
        //resultList.stream().forEach(data -> data.dump());

        return resultList;
    }
    */
}
