package ru.javawebinar.topjava.repository;

import java.util.Collection;
import java.util.List;

public interface Repository <V, K>{
	V save(V v);
	V get(K v);
	Collection<V> getAll();
	V delete(K k);
}
