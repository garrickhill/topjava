package ru.javawebinar.topjava;

import javax.validation.groups.Default;

public class View {
    public interface JsonUI {}
    public interface JsonREST {}
    public interface ValidatedUI {}
    public interface Persist extends Default {}
}