import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.FileInputStream;

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
                );
                mealDataList.add(md);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // csvファイルを読み込む

}
