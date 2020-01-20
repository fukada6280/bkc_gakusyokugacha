import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Knapsack {

    public static List<MealData> filterVal(final List<MealData> mealDataList, final int maxValue) {
        final List<MealData> filterList =
                mealDataList.stream()
                        .filter(mealData -> mealData.getValue() <= maxValue)
                        .collect(Collectors.toList());
        return filterList;
    }

}
