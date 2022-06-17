package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository){
        this.repository = repository;
    }

    public Meal get(int id, int userId) {
        Meal meal = repository.get(id, userId);
        if(meal == null) throw new IllegalArgumentException();
        return meal;
    }

    public boolean delete(int id, int userId) {
        return repository.delete(id, userId);
    }

    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getBetweenDates(LocalDate startDate, LocalDate endDate, int userId) {
        return repository.getBetween(startDate, endDate, userId);
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void update(Meal meal, int userId) {
        if(meal.getUserId() != userId)
            throw new IllegalArgumentException();
        repository.save(meal, userId);
    }
}