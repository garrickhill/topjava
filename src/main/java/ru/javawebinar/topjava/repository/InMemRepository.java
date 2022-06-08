package ru.javawebinar.topjava.repository;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class InMemRepository<V, K> implements Repository<V, K> {
	private Map<K,V> map = new HashMap<>();
	
	@Override
	public V save(V v) {
		K key = getKey(v);
		if(!map.containsKey(key)) { 
			key = nextKey();
			setKeyFor(v, key);
		}
		return map.put(key,v);
	}

	@Override
	public V delete(K k) {
		return map.remove(k);
	}
	
	@Override
	public V get(K k) {
		return map.get(k);
	}

	@Override
	public Collection<V> getAll() {
		return map.values();
	}
	
	K getKey(V v) {
		for(Field f: v.getClass().getDeclaredFields()) {
			Key id = f.getAnnotation(Key.class);
			if(id!=null) {
				f.setAccessible(true);
				try {
					return (K) f.get(v);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	void setKeyFor(V v, K k) {
		for(Field f: v.getClass().getDeclaredFields()) {
			Key id = f.getAnnotation(Key.class);
			if(id!=null) {
				f.setAccessible(true);
				try {
					f.set(v,k);
					break;
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected abstract K nextKey();

}
