import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
<<<<<<< HEAD

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // 読み込むファイルの名前
        String filename = "mealData.csv";
        File file = new File(filename);
        List<MealData> mealDataList = new ArrayList<>(); // メインのデータとなる


=======
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame {
    public static void main(String[] args) {
        // csvファイルを読み込む
        List<MealData> mealDataList = loadCsv("mealData.csv");

        MainFrame mf = new MainFrame(); // mainFrame
        mf.setDefaultCloseOperation(EXIT_ON_CLOSE); // xで消せるように
        mf.setTitle("BKC学食ガチャ"); // タイトルを定義
        mf.setSize(360, 640); // フレームの大きさを定義
        mf.setVisible(true); // 可視化する

        // 適当に絞り込み要件を定義 (本当はユーザーに入力させたい)
        String priorityPlace = "ユニオン１階"; // リンク優先
        int maxValue = 500;
        int priorityColumn = 4; // カロリー優先


        // ナップサック問題に受け渡す引数を作成
        // 1. 小皿リストを作成
        List<MealData> kozaraList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getPlace().equals(priorityPlace))
                        .filter(mealData -> mealData.getCategory().equals("小皿"))
                        .collect(Collectors.toList());

        // 2. 米と米なしメインを結合したメイン料理のみのリストを作成
        List<MealData> mainList = makeMainDishList(mealDataList, priorityPlace, maxValue);

        // 3. 2を用いて小鉢に充てるお金をもつリストを作成
        List<Integer> moneyApplyInKozaraList =
                mainList.stream()
                        .map(mealData -> maxValue - mealData.getValue())
                        .collect(Collectors.toList());

        // ナップサック問題を応用してそれぞれのメイン料理にふさわしい小鉢のインデックスリストを記録
        List<List<Integer>> recommendList = new ArrayList<>();
        for (Integer moneyApplyInKozara : moneyApplyInKozaraList) {
            List<Integer> tmp = Knapsack.filterVal(kozaraList, moneyApplyInKozara);
            recommendList.add(tmp);
        }

        // 計算が正しいか確認 → 多分合ってる
        int i=0;
        for (List<Integer> recommend : recommendList) {
            System.out.print("使えるお金が " + moneyApplyInKozaraList.get(i) + "円 のとき組み合わせは: ");
            for (Integer re : recommend) {
                System.out.print(re + ", ");
            }
            System.out.println();
            i++;
        }

        // メインと小皿を結合する


        /* 価格を降順に並べる(正しく動く)
        mealDataList.stream()
                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                .forEach(System.out::println);
         */

        // 上位２割ほどの中からランダムに一つ組み合わせを決定（"ガチャ"なのでね）



        // とりあえずはターミナルに表示させる



    }

    /**
     * csvファイルを読み込む
     * @param filename 開きたいcsvファイルの名前
     * @return MealData型のリスト
      */
    public static List<MealData> loadCsv(String filename) {
        File file = new File(filename);
        List<MealData> mealDataList = new ArrayList<>(); // メインのデータとなる

>>>>>>> origin/master
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
<<<<<<< HEAD
                        row[0],
                        row[1],
                        row[2],
                        Integer.parseInt(row[3]),
                        Double.parseDouble(row[4]),
                        Double.parseDouble(row[5]),
                        Double.parseDouble(row[6]),
                        Double.parseDouble(row[7]),
                        Double.parseDouble(row[8]),
                        Integer.parseInt(row[9]),
                        Integer.parseInt(row[10])
=======
                        row[0], row[1], row[2],
                        Integer.parseInt(row[3]), Double.parseDouble(row[4]),
                        Double.parseDouble(row[5]), Double.parseDouble(row[6]),
                        Double.parseDouble(row[7]), Double.parseDouble(row[8]),
                        Integer.parseInt(row[9]), Integer.parseInt(row[10])
>>>>>>> origin/master
                );
                mealDataList.add(md);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
<<<<<<< HEAD
        // ここから絞り込みを始める
        // 適当に絞り込み要件を定義
        String priorityPlace = "リンク"; // リンク優先
        int maxValue = 100;
        int priorityColumn = 4; // カロリー優先

        // 計算量を減らすため、場所限定と価格以下のフィルタをかける
        for (MealData mealData : mealDataList) {
            //
        }

        // maxValue以下のものだけ出力(→完成している)
        mealDataList.stream()
                .filter(mealData -> mealData.getValue() <= maxValue)
                .forEach(mealData -> mealData.dump());

        // 場所が等しいものだけ出力(→完成している)
        mealDataList.stream()
                .filter(mealData -> mealData.getPlace().equals(priorityPlace))
                .forEach(mealData -> mealData.dump());

        // maxValue以下のものだけを抽出
        List<MealData> filterList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getValue() <= maxValue)
                        .collect(Collectors.toList());

        filterList.stream()
                .forEach(data -> data.dump());


        // 残ったものの情報を増大させる

        // 全通りのリストができたと仮定する(上でcomplateListを作るとすると２行目を変更！！)

        //answerList.sort

        // 求めたリストを出力する

    }

    //
}

=======
        return mealDataList;
    }


    /**
     * 米なしメイン+米=メインとするリストを出力, ついでに場所と価格も絞り込んでおく
     * @param mealDataList 加工したいリスト
     * @param priorityPlace 絞り込み条件：場所
     * @param maxValue 絞り込み条件：価格
     * @return 区分がメインのみのリストを出力
     */
    public static List<MealData> makeMainDishList(List<MealData> mealDataList, String priorityPlace, int maxValue) {
        // 計算量を減らすため、先に場所限定と価格以下のフィルタをかける
        List<MealData> filterList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getPlace().equals(priorityPlace))
                        .filter(mealData -> mealData.getValue() <= maxValue)
                        .collect(Collectors.toList());

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

        // 米なしメイン+米=メイン を作成する
        for (MealData noRiceMainData : noRiceMainList) {
            for (MealData riceData : riceList) {
                MealData mealData = noRiceMainData.combine(riceData); // 米なしメイン+米=メイン を作成
                mainList.add(mealData);
            }
        }
        return mainList;
    }


}
>>>>>>> origin/master
