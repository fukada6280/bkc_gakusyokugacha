import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main extends JFrame {

    public static void main(String[] args) {

        MainFrame mf = new MainFrame(); // mainFrame
        mf.setDefaultCloseOperation(EXIT_ON_CLOSE); // xで消せるように
        mf.setTitle("BKC学食ガチャ"); // タイトルを定義
        mf.setSize(360, 640); // フレームの大きさを定義
        mf.setVisible(true); // 可視化する

        // csvファイルを読み込む
        // List<MealData> mealDataList = loadCsv("mealData.csv");


    }


    /**
     * 元データから条件にあったメニューの組み合わせを取ってくる, 取ってくる小皿はばらつきがある
     * @param mealDataList 絞り込みたい元データ
     * @param priorityPlace 絞り込みたい場所
     * @param maxValue この値以下のメニューを取ってくる
     * @param priorityColumn
     * @return 条件にあったメニュー１つ
     */
    public static MealData gacha(List<MealData> mealDataList, String priorityPlace, int maxValue, String priorityColumn) {
        Random rand = new Random();
        // ナップサック問題に受け渡す引数を作成
        // 1. 小皿リストを作成
        List<MealData> kozaraList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getPlace().equals(priorityPlace))
                        .filter(mealData -> mealData.getCategory().equals("小皿"))
                        .collect(Collectors.toList());

        // 2. 米と米なしメインを結合したメイン料理のみのリストを作成
        List<MealData> mainList = makeMainDishList(mealDataList, priorityPlace, maxValue);
        mainList = mainList.stream()
                        .filter(mealData -> mealData.getValue() <= maxValue)
                        .collect(Collectors.toList());

        // 3. 2を用いて小鉢に充てるお金をもつリストを作成
        List<Integer> moneyApplyInKozaraList =
                mainList.stream()
                        .map(mealData -> maxValue - mealData.getValue())
                        .collect(Collectors.toList());

        //System.out.println("残りのお金リスト");
        //moneyApplyInKozaraList.stream()
        //        .forEach(System.out::println);


        // それぞれのメイン料理にふさわしい小鉢のインデックスリストを記録
        List<List<Integer>> recommendList = new ArrayList<>();
        for (Integer moneyApplyInKozara : moneyApplyInKozaraList) {
            List<Integer> tmp = Knapsack.filterVal(kozaraList, moneyApplyInKozara);
            recommendList.add(tmp);
        }

        /* 計算が正しいか確認
        for (int i = 0; i < recommendList.size(); i++) {
            System.out.print("使えるお金が " + moneyApplyInKozaraList.get(i) + "円 のとき組み合わせは: ");
            for (Integer re : recommendList.get(i)) {
                System.out.print(re + ", ");
            }
            System.out.println();
        } */

        // メインと小皿を結合する , mainListとrecommendListの結合
        // メニューひとつひとつのループ
        for (int i = 0; i < recommendList.size(); i++) {
            // 小皿ひとつひとつのループ mainListに小皿を結合していく
            List<MealData> resultMainMealData = new ArrayList<>();
            for (Integer re : recommendList.get(i)) {
                // 小皿を値段でフィルタしてその中からランダムで取り出す -> 厳密ではなくなってしまう
                List<MealData> filterKozaraList =
                        kozaraList.stream()
                                .filter(mealData -> mealData.getValue() == re)
                                .collect(Collectors.toList());
                // ランダムで一つ取り出す
                MealData resultOneKozaraData =
                        filterKozaraList.get(rand.nextInt(filterKozaraList.size()));
                //System.out.print(mainList.get(i).getName() + "=> ");
                //System.out.print("+" + resultOneKozaraData.getName() + ", ");
                mainList.set(i, mainList.get(i).combine(resultOneKozaraData)); // メイン料理に小皿を追加
            }
        }

        // ガチャ結果にふさわしいメニューを抽出、この時点で必ず価格はmaxValue以下になっているので価格フィルタはかけない
        // 価格順にソートしたあと、優先条件でソートする
        // 頭悪いが、ジェネリクスがうまく適用できないのでとりあえず分岐させている　要修正
        List<MealData> resultMealDataList = new ArrayList<>();
        switch (priorityColumn) {
            case "カロリーを低く":
                resultMealDataList =
                        mainList.stream()
                                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                                .sorted(Comparator.comparingDouble(MealData::getKcal)) // カロリーの昇順
                                .collect(Collectors.toList());
                break;
            case "カロリーを高く":
                resultMealDataList =
                        mainList.stream()
                                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                                .sorted(Comparator.comparingDouble(MealData::getKcal).reversed()) // カロリーの降順
                                .collect(Collectors.toList());
                break;
            case "タンパク質を大きく":
                resultMealDataList =
                        mainList.stream()
                                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                                .sorted(Comparator.comparingDouble(MealData::getProtein).reversed()) // タンパク質の降順
                                .collect(Collectors.toList());
                break;
            case "脂質を小さく":
                resultMealDataList =
                        mainList.stream()
                                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                                .sorted(Comparator.comparingDouble(MealData::getLipid)) // 脂質の昇順
                                .collect(Collectors.toList());
                break;
            case "カルシウムを大きく":
                resultMealDataList =
                        mainList.stream()
                                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                                .sorted(Comparator.comparingDouble(MealData::getCalcium).reversed()) // カルシウムの降順
                                .collect(Collectors.toList());
                break;
            case "野菜量を大きく":
                resultMealDataList =
                        mainList.stream()
                                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                                .sorted(Comparator.comparingDouble(MealData::getVegetable).reversed()) // 野菜量の降順
                                .collect(Collectors.toList());
                break;
            case "ガチャ(ランダム)":
            case "予算に近く":
                resultMealDataList =
                        mainList.stream()
                                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                                .collect(Collectors.toList());
                break;
            default:
                System.out.println("入力された優先項目は存在しません");
                resultMealDataList =
                        mainList.stream()
                                .sorted(Comparator.comparingInt(MealData::getValue).reversed())
                                .collect(Collectors.toList());
        }

        // 「指定しない」ときはここでランダムに選ぶ
        if (priorityColumn.equals("ガチャ(ランダム)")) {
            return resultMealDataList.get(rand.nextInt(resultMealDataList.size())); // List内のランダムなメニューを選択
        }

        return resultMealDataList.get(0);
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


    /**
     * 米なしメイン+米=メインとするリストを出力, ついでに場所と価格も絞り込んでおく
     * @param mealDataList 加工したいリスト
     * @param priorityPlace 絞り込み条件：場所
     * @param maxValue 絞り込み条件：価格
     * @return 区分がメインのみのリストを出力
     */
    private static List<MealData> makeMainDishList(List<MealData> mealDataList, String priorityPlace, int maxValue) {
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
