package ua.kpi.tef.util;

import ua.kpi.tef.model.UserMeal;
import ua.kpi.tef.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);



    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {


        Map<LocalDate, Integer> map = new HashMap<>();

        mealList.forEach(meal -> {
            LocalDate userDay = LocalDate.of(meal.getDateTime().getYear(), meal.getDateTime().getMonth(), meal.getDateTime().getDayOfMonth()/*, meal.getDateTime().getHour()*/);
            Integer mealCalories = meal.getCalories();
            map.computeIfPresent(userDay, (k, v) -> {return v += mealCalories;});
            map.putIfAbsent(userDay, mealCalories);
        });

        List<UserMealWithExceed> exceedList = new ArrayList<UserMealWithExceed> ();

        mealList.forEach(dayMeal -> {
            boolean isExceeded = (map.get(dayMeal.getDateTime().toLocalDate()) > caloriesPerDay);
            if (TimeUtil.isBetween(dayMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                exceedList.add(new UserMealWithExceed(dayMeal.getDateTime(), dayMeal.getDescription(), dayMeal.getCalories(), isExceeded));
            } //redo to merge

        });
  
        return exceedList;
    }
}
