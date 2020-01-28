import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
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

        // 適当に絞り込み要件を定義 (本当はユーザーに入力させたい)
        String priorityPlace = "ユニオン１階"; // リンク優先
        int maxValue = 500;
        int priorityColumn = 4; // カロリー優先


        // ryuseiくんに受け渡す引数を作成
        // 小皿リストを作成 (ryuseiくんへ受け渡す)
        List<MealData> kozaraList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getPlace().equals(priorityPlace))
                        .filter(mealData -> mealData.getCategory().equals("小皿"))
                        .collect(Collectors.toList());

        // 米と米なしメインを結合したメイン料理のみのリストを作成
        List<MealData> mainList = makeMainDishList(mealDataList, priorityPlace, maxValue);

        // 小鉢に充てるお金をもつリストを作成
        List<Integer> moneyApplyInKozaraList =
                mainList.stream()
                        .map(mealData -> maxValue - mealData.getValue())
                        .collect(Collectors.toList());

        // ナップサック問題を応用してそれぞれのメイン料理にふさわしい小鉢のインデックスリストを記録
        List<List<Integer>> recommendList = new ArrayList<>();
        for (Integer moneyApplyInKozara : moneyApplyInKozaraList) {
            List<Integer> tmp = new ArrayList<>();
            tmp = Knapsack.filterVal(kozaraList, moneyApplyInKozara);
            recommendList.add(tmp);
        }

        // 正しいか確認
        //recommendList.stream().forEach(System.out::println);

        // 正しいか確認２
        int i=0;
        for (List<Integer> recommend : recommendList) {
            System.out.print("使えるお金は " + moneyApplyInKozaraList.get(i) + "円 のとき組み合わせは: ");
            for (Integer re : recommend) {
                System.out.print(re + ", ");
            }
            System.out.println();
            i++;
        }

        // メインと小皿を結合する

        // 上位２割ほどの中からランダムに一つ組み合わせを決定（"ガチャ"なのでね）

        /* お試し実行 価格を昇順に並べる(正しく動く)
        /mealDataList.stream()
                .sorted(Comparator.comparingInt(MealData::getValue))
                .forEach(System.out::println);
         */

        /* お試し実行 価格を降順に並べる(正しく動く)
        mealDataList.stream()
                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                .forEach(System.out::println);
         */
        // 一度しか呼び出されないのでここで設定
        MainFrame mf = new MainFrame(); // mainFrame
        mf.setDefaultCloseOperation(EXIT_ON_CLOSE); // xで消せるように
        mf.setTitle("BKC学食ガチャ"); // タイトルを定義
        mf.setSize(360, 640); // フレームの大きさを定義
        mf.setVisible(true); // 可視化する

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
    米なしリスト + 米 = メイン に結合されたリストを出力
     */

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

        List<MealData> kozaraList =
                filterList.stream()
                        .filter(mealData -> mealData.getCategory().equals("小皿"))
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
