package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class InMemMealRepository extends InMemRepository<Meal, Integer> {
    AtomicInteger generator = new AtomicInteger(0);

    public InMemMealRepository(){
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    protected Integer nextKey() {
        return generator.incrementAndGet();
    }
}
