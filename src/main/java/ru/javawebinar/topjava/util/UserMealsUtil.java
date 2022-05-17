package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByStreamsOptionals(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        final Map<LocalDate, Integer> sumByDate = new HashMap<>();
        meals.forEach(meal -> sumByDate.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));

        final List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(UserMealWithExcess.fromUserMeal(meal, sumByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<LocalDate, Integer> subByDate = meals.stream()
                .collect(
                        Collectors.toMap(u -> u.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(m -> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> UserMealWithExcess.fromUserMeal(meal, subByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsOptionals(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        class Acc {
            int totalCalories = 0;
            List<UserMeal> meals = new ArrayList<>();

            Acc(UserMeal meal) {
                add(meal);
            }

            Acc merge(Acc acc) {
                totalCalories += acc.totalCalories;
                meals.addAll(acc.meals);
                return this;
            }

            void add(UserMeal userMeal) {
                totalCalories += userMeal.getCalories();
                if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                    meals.add(userMeal);
            }

            Stream<UserMealWithExcess> convert() {
                return meals
                        .stream()
                        .map(m -> UserMealWithExcess.fromUserMeal(m, totalCalories > caloriesPerDay));
            }
        }

        return meals.stream().collect(
                        Collectors.toMap(u -> u.getDateTime().toLocalDate(), Acc::new, Acc::merge)
                )
                .values().stream()
                .flatMap(v -> v.convert())
                .collect(Collectors.toList());
    }
}
