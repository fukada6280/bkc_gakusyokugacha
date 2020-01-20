import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Knapsack {

    /**
     * リストを指定価格以下にフィルタする
     * @param mealDataList 用いるリスト, MealData型のみ対応
     * @param maxValue フィルタしたい価格
     * @return 指定価格以下にフィルタされたリスト
     */
    public static List<MealData> filterVal(final List<MealData> mealDataList, final int maxValue) {
        final List<MealData> filterList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getValue() <= maxValue)
                        .collect(Collectors.toList());
        return filterList;
    }

}
